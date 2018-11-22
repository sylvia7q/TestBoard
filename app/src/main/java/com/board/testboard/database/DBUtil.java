package com.board.testboard.database;

import android.content.Context;
import android.util.Log;

import com.board.testboard.bean.KANBAN_MC_PATTEM;
import com.board.testboard.bean.KANBAN_USER_PATTEM;
import com.board.testboard.bean.LINE_DISPLAY_TIME;
import com.board.testboard.bean.LINE_STATION;
import com.board.testboard.bean.SMT_BOARD_LINE;
import com.board.testboard.bean.SMT_WORKSHOP_LINE_SETTING;
import com.board.testboard.bean.UserBean;
import com.board.testboard.utils.LogUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import java.util.List;

public class DBUtil {

    private final static String TAG = DBUtil.class.getName();
    public static final String DB_NAME = "kanban";
    public static final int DB_VERSION = 1;
    private DbUtils mDbUtils;

    public DBUtil(Context context) {
        super();
        //mDbUtils = DbUtils.create(context, DBName);
        mDbUtils = DbUtils.create(context, DB_NAME, DB_VERSION, new DbUtils.DbUpgradeListener() {

            @Override
            public void onUpgrade(DbUtils dbUtils, int oldVersion, int newVersion) {
                // TODO Auto-generated method stub
                Log.e(TAG, "-----------------oldVersion:" + oldVersion + "   newVersion:" + newVersion);
            }
        });
        mDbUtils.configAllowTransaction(false);
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    public boolean saveUser(UserBean user) {
        boolean flag = false;
        try {
            UserBean object = getUserById(user.getUserId());
            if (object == null) {
                LogUtil.e(TAG,"add user");
                mDbUtils.save(user);
            } else {
                LogUtil.e(TAG,"update user");
                mDbUtils.update(user);
            }
            flag = true;
        } catch (DbException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询已经登陆的账户
     * @return
     */
    public UserBean getLatestUser() {
        UserBean user = null;
        try {
            user = mDbUtils.findFirst(Selector.from(UserBean.class).where("curAccount","=","Y"));
            if(user != null){
                return user;
            }else {
                return null;
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * 查询所有账户,按最新登陆时间排序
     * @return
     */
    public List<UserBean> getAllUser() {
        List<UserBean> userBeans = null;
        try {
            userBeans = mDbUtils.findAll(Selector.from(UserBean.class).orderBy("loginTime", true));
            if(userBeans != null){
                return userBeans;
            }else {
                return null;
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    /**
     * 根据ID获取用户
     *
     * @param userId
     * @return
     */
    public UserBean getUserById(String userId) {
        try {
            return mDbUtils.findFirst(Selector.from(UserBean.class).where("userId", "=", userId));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    /**
     * 新增用户模板
     * @param userPattem
     * @return
     */
    public boolean saveUserPattem(KANBAN_USER_PATTEM userPattem) {
        boolean flag = false;
        try {
            KANBAN_USER_PATTEM object = getUserPattemByNo(userPattem.getPATTEM_NO());
            if (object == null) {
                LogUtil.e(TAG,"add userPattem");
                mDbUtils.save(userPattem);
            } else {
                LogUtil.e(TAG,"update userPattem");
                mDbUtils.update(userPattem);
            }
            flag = true;
        } catch (DbException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 获取所有用户模板
     * @return
     */
    public List<KANBAN_USER_PATTEM> getAllUserPattem() {
        List<KANBAN_USER_PATTEM> userPattems = null;
        try {
            userPattems = mDbUtils.findAll(Selector.from(KANBAN_USER_PATTEM.class).orderBy("SORTID", true));
            if(userPattems != null){
                return userPattems;
            }else {
                return null;
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    /**
     * 根据ID获取模板
     *
     * @param pattemNo
     * @return
     */
    public KANBAN_USER_PATTEM getUserPattemByNo(String pattemNo) {
        try {
            return mDbUtils.findFirst(Selector.from(KANBAN_USER_PATTEM.class).where("PATTEM_NO", "=", pattemNo));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    /**
     * 根据ID获取模板
     *
     * @param internalCode
     * @return
     */
    public KANBAN_USER_PATTEM getUserPattemByCode(String internalCode) {
        try {
            return mDbUtils.findFirst(Selector.from(KANBAN_USER_PATTEM.class).where("INTERNAL_CODE", "=", internalCode));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    /**
     * 删除用户模板
     *
     * @param  userPattem
     * @return
     */
    public boolean deleteUserPattem(KANBAN_USER_PATTEM userPattem) {

        boolean flag;
        try {
            KANBAN_USER_PATTEM object = getUserPattemByNo(userPattem.getPATTEM_NO());
            if (object != null) {
                LogUtil.e(TAG,"delete " + object.toString());
                mDbUtils.delete(userPattem);
            } else {
                LogUtil.e(TAG,userPattem.toString() + " is not exists");
            }
            flag = true;
        } catch (Exception e) {
            // TODO: handle exception
            flag = false;
        }
        return flag;
    }
    /**
     * 新增展示模板
     * @param mcPattem
     * @return
     */
    public boolean saveMcPattem(KANBAN_MC_PATTEM mcPattem) {
        boolean flag = false;
        try {
            KANBAN_MC_PATTEM object = getMcPattemByNo(mcPattem.getPATTEM_NO());
            if (object == null) {
                LogUtil.e(TAG,"add mcPattem");
                mDbUtils.save(mcPattem);
            } else {
                LogUtil.e(TAG,"update mcPattem");
                mDbUtils.update(mcPattem);
            }
            flag = true;
        } catch (DbException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 查询所有账户,按最新登陆时间排序
     * @return
     */
    public List<KANBAN_MC_PATTEM> getAllMcPattem() {
        List<KANBAN_MC_PATTEM> mcPattems = null;
        try {
            mcPattems = mDbUtils.findAll(Selector.from(KANBAN_MC_PATTEM.class).orderBy("SORTID", false));
            if(mcPattems != null){
                return mcPattems;
            }else {
                return null;
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * 根据ID获取模板
     *
     * @param pattemNo
     * @return
     */
    public KANBAN_MC_PATTEM getMcPattemByNo(String pattemNo) {
        try {
            return mDbUtils.findFirst(Selector.from(KANBAN_MC_PATTEM.class).where("PATTEM_NO", "=", pattemNo));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    /**
     * 删除展示模板
     *
     * @param  mcPattem
     * @return
     */
    public boolean deleteMCPattem(KANBAN_MC_PATTEM mcPattem) {

        boolean flag;
        try {
            KANBAN_MC_PATTEM object = getMcPattemByNo(mcPattem.getPATTEM_NO());
            if (object != null) {
                LogUtil.e(TAG,"delete " + object.toString());
                mDbUtils.delete(mcPattem);
            } else {
                LogUtil.e(TAG,mcPattem.toString() + " is not exists");
            }
            flag = true;
        } catch (Exception e) {
            // TODO: handle exception
            flag = false;
        }
        return flag;
    }
    /**
     * 根据ID获取模板
     *
     * @param internalCode
     * @return
     */
    public KANBAN_MC_PATTEM getMcPattemByCode(String internalCode) {
        try {
            return mDbUtils.findFirst(Selector.from(KANBAN_MC_PATTEM.class).where("INTERNAL_CODE", "=", internalCode));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    /**
     * 根据ID获取模板
     *
     * @param sortId
     * @return
     */
    public KANBAN_MC_PATTEM getMcPattemBySortId(String sortId) {
        try {
            return mDbUtils.findFirst(Selector.from(KANBAN_MC_PATTEM.class).where("SORTID", "=", sortId));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    /**
     * 新增线别
     * @param moreLine
     * @return
     */
    public boolean saveSMT_BOARD_LINE(SMT_BOARD_LINE moreLine) {
        boolean flag = false;
        try {
            SMT_BOARD_LINE object = getLineByNo(moreLine.getLINE_NO());
            if (object == null) {
                LogUtil.e(TAG,"add SMT_BOARD_LINE");
                mDbUtils.save(moreLine);
            } else {
                LogUtil.e(TAG,"update SMT_BOARD_LINE");
                mDbUtils.update(moreLine);
            }
            flag = true;
        } catch (DbException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据ID获取线别信息
     *
     * @param lineNo
     * @return
     */
    public SMT_BOARD_LINE getLineByNo(String lineNo) {
        try {
            return mDbUtils.findFirst(Selector.from(SMT_BOARD_LINE.class).where("LINE_NO", "=", lineNo));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    /**
     * 获取所有线别
     * @return
     */
    public List<SMT_BOARD_LINE> getAllLine() {
        List<SMT_BOARD_LINE> moreLines = null;
        try {
            moreLines = mDbUtils.findAll(Selector.from(SMT_BOARD_LINE.class));
            if(moreLines != null){
                return moreLines;
            }else {
                return null;
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    /**
     * 根据ID获取线别信息
     *
     * @param lineNo
     * @return
     */
    public SMT_WORKSHOP_LINE_SETTING getWorkshopLineByNo(String lineNo) {
        try {
            return mDbUtils.findFirst(Selector.from(SMT_WORKSHOP_LINE_SETTING.class).where("LINE_NO", "=", lineNo));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    /**
     * 获取所有线别
     * @return
     */
    public List<SMT_WORKSHOP_LINE_SETTING> getAllWorkshopLine() {
        List<SMT_WORKSHOP_LINE_SETTING> moreLines = null;
        try {
            moreLines = mDbUtils.findAll(Selector.from(SMT_WORKSHOP_LINE_SETTING.class));
            if(moreLines != null){
                return moreLines;
            }else {
                return null;
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    /**
     * 新增线别
     * @param moreLine
     * @return
     */
    public boolean saveSMT_WORKSHOP_LINE_SETTING(SMT_WORKSHOP_LINE_SETTING moreLine) {
        boolean flag = false;
        try {
            SMT_WORKSHOP_LINE_SETTING object = getWorkshopLineByNo(moreLine.getLINE_NO());
            if (object == null) {
                LogUtil.e(TAG,"add SMT_BOARD_LINE");
                mDbUtils.save(moreLine);
            } else {
                LogUtil.e(TAG,"update SMT_BOARD_LINE");
                mDbUtils.update(moreLine);
            }
            flag = true;
        } catch (DbException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }
    public boolean deleteAllSMT_WORKSHOP_LINE_SETTING(){
        boolean flag;
        List<SMT_WORKSHOP_LINE_SETTING> smtBoardLines = null;
        try {
            smtBoardLines = mDbUtils.findAll(SMT_WORKSHOP_LINE_SETTING.class);
            if (smtBoardLines != null) {
                LogUtil.e(TAG,"delete all SMT_WORKSHOP_LINE_SETTING");
                mDbUtils.deleteAll(smtBoardLines);
            } else {
                LogUtil.e(TAG,"is not exists SMT_WORKSHOP_LINE_SETTING table");
            }
            flag = true;

        } catch (Exception e) {
            // TODO: handle exception
            flag = false;
        }
        return flag;
    }
    /**
     * 删除一条线别的所有信息
     * @param smtBoardLine
     * @return
     */
    public boolean deleteSMT_BOARD_LINE(SMT_BOARD_LINE smtBoardLine){
        boolean flag;
        try {
            SMT_BOARD_LINE boardLineObject = getLineByNo(smtBoardLine.getLINE_NO());
//            LINE_STATION lineStationObject = getStationByLine(smtBoardLine.getLINE_NO());
            LINE_DISPLAY_TIME lineDisplayTimeObject = getDisPlayByLine(smtBoardLine.getLINE_NO());
            if (boardLineObject != null) {
                LogUtil.e(TAG,"delete SMT_BOARD_LINE");
                mDbUtils.delete(smtBoardLine);
            } else {
                LogUtil.e(TAG,smtBoardLine.getLINE_NO() + " is not exists SMT_BOARD_LINE table");
            }
//            if (lineStationObject != null) {
//                LogUtil.e(TAG,"delete LINE_STATION");
//                deleteLINE_STATION(smtBoardLine.getLINE_NO());
//            } else {
//                LogUtil.e(TAG,smtBoardLine.getLINE_NO() + " is not exists LINE_STATION table");
//            }
            if (lineDisplayTimeObject != null) {
                LogUtil.e(TAG,"delete LINE_DISPLAY_TIME");
                deleteLINE_DISPLAY_TIME(smtBoardLine.getLINE_NO());
            } else {
                LogUtil.e(TAG,smtBoardLine.getLINE_NO() + " is not exists LINE_DISPLAY_TIME table");
            }
            flag = true;

        } catch (Exception e) {
            // TODO: handle exception
            flag = false;
        }
        return flag;
    }
    public boolean deleteAllSMT_BOARD_LINE(){
        boolean flag = false;
        List<SMT_BOARD_LINE> smtBoardLines = null;
        try {
            smtBoardLines = mDbUtils.findAll(SMT_BOARD_LINE.class);
            if (smtBoardLines != null) {
                LogUtil.e(TAG,"delete all SMT_BOARD_LINE");
                mDbUtils.deleteAll(smtBoardLines);
            } else {
                LogUtil.e(TAG,"SMT_BOARD_LINE is not exists SMT_BOARD_LINE table");
            }
            flag = true;

        } catch (Exception e) {
            // TODO: handle exception
            flag = false;
        }
        return flag;
    }
    /**
     * 新增线别
     * @param lineStation
     * @return
     */
    public boolean saveLINE_STATION(LINE_STATION lineStation) {
        boolean flag = false;
        try {
            LINE_STATION object = getStationByLine(lineStation.getLINE_NO());
            if (object == null) {
                LogUtil.e(TAG,"add LINE_STATION");
                mDbUtils.save(lineStation);
            } else {
                LogUtil.e(TAG,"update LINE_STATION");
                mDbUtils.update(lineStation);
            }
            flag = true;
        } catch (DbException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 获取所有线别
     * @return
     */
    public List<LINE_STATION> getAllStation() {
        List<LINE_STATION> lineStations = null;
        try {
            lineStations = mDbUtils.findAll(Selector.from(LINE_STATION.class));
            if(lineStations != null){
                return lineStations;
            }else {
                return null;
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    /**
     * 根据ID获取线别信息
     *
     * @param lineNo
     * @return
     */
    public LINE_STATION getStationByLine(String lineNo) {
        try {
            return mDbUtils.findFirst(Selector.from(LINE_STATION.class).where("LINE_NO", "=", lineNo));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    /**
     * 删除线别对应的站位
     * @param lineStation
     * @return
     */
    public boolean deleteLINE_STATION(LINE_STATION lineStation){
        boolean flag = false;
        try {
            LINE_STATION object = getStationByLine(lineStation.getLINE_NO());
            if (object != null) {
                LogUtil.e(TAG,"delete LINE_STATION");
                mDbUtils.delete(lineStation);
            } else {
                LogUtil.e(TAG,lineStation.getLINE_NO() + " is not exists");
            }
            flag = true;
        } catch (Exception e) {
            // TODO: handle exception
            flag = false;
        }
        return flag;
    }
    /**
     * 删除线别对应的站位
     * @param lineStation
     * @return
     */
    public boolean deleteLINE_STATION(String lineStation){
        boolean flag = false;
        try {
            LINE_STATION object = getStationByLine(lineStation);
            if (object != null) {
                LogUtil.e(TAG,"delete LINE_STATION");
                mDbUtils.delete(lineStation);
            } else {
                LogUtil.e(TAG,lineStation + " is not exists");
            }
            flag = true;
        } catch (Exception e) {
            // TODO: handle exception
            flag = false;
        }
        return flag;
    }
    /**
     * 新增线别
     * @param displayTime
     * @return
     */
    public boolean saveLINE_DISPLAY_TIME(LINE_DISPLAY_TIME displayTime) {
        boolean flag = false;
        try {
            LINE_DISPLAY_TIME object = getDisPlayByLine(displayTime.getLINE_NO());
            if (object == null) {
                LogUtil.e(TAG,"add LINE_DISPLAY_TIME");
                mDbUtils.save(displayTime);
            } else {
                LogUtil.e(TAG,"update LINE_DISPLAY_TIME");
                mDbUtils.update(displayTime);
            }
            flag = true;
        } catch (DbException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 获取所有线别
     * @return
     */
    public List<LINE_DISPLAY_TIME> getAllLineDisplayTime() {
        List<LINE_DISPLAY_TIME> displayTimeLists = null;
        try {
            displayTimeLists = mDbUtils.findAll(Selector.from(LINE_DISPLAY_TIME.class));
            if(displayTimeLists != null){
                return displayTimeLists;
            }else {
                return null;
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    /**
     * 根据ID获取线别信息
     *
     * @param lineNo
     * @return
     */
    public LINE_DISPLAY_TIME getDisPlayByLine(String lineNo) {
        try {
            return mDbUtils.findFirst(Selector.from(LINE_DISPLAY_TIME.class).where("LINE_NO", "=", lineNo));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * 删除
     * @param lineDisplayTime
     * @return
     */
    public boolean deleteLINE_DISPLAY_TIME(LINE_DISPLAY_TIME lineDisplayTime){
        boolean flag = false;
        try {
            LINE_DISPLAY_TIME object = getDisPlayByLine(lineDisplayTime.getLINE_NO());
            if (object != null) {
                LogUtil.e(TAG,"delete LINE_DISPLAY_TIME");
                mDbUtils.delete(lineDisplayTime);
            } else {
                LogUtil.e(TAG,lineDisplayTime.getLINE_NO() + " is not exists");
            }
            flag = true;
        } catch (Exception e) {
            // TODO: handle exception
            flag = false;
        }
        return flag;
    }
    /**
     * 删除
     * @param lineDisplayTime
     * @return
     */
    public boolean deleteLINE_DISPLAY_TIME(String lineDisplayTime){
        boolean flag = false;
        try {
            LINE_DISPLAY_TIME object = getDisPlayByLine(lineDisplayTime);
            if (object != null) {
                LogUtil.e(TAG,"delete LINE_DISPLAY_TIME");
                mDbUtils.delete(lineDisplayTime);
            } else {
                LogUtil.e(TAG,lineDisplayTime + " is not exists");
            }
            flag = true;
        } catch (Exception e) {
            // TODO: handle exception
            flag = false;
        }
        return flag;
    }
    /**
     * 新增线别
     * @param attrBean
     * @return
     *//*
    public boolean saveMessageAttrBean(MessageAttrBean attrBean) {
        boolean flag;
        try {
            MessageAttrBean object = getMsgByType(attrBean.getMSG_TYPE());
            if (object == null) {
                LogUtil.e(TAG,"add MessageAttrBean");
                mDbUtils.save(attrBean);
            } else {
                LogUtil.e(TAG,"update MessageAttrBean");
                mDbUtils.update(attrBean);
            }
            flag = true;
        } catch (DbException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }


    *//**
     * 获取所有消息类型
     * @return
     *//*
    public List<MessageAttrBean> getAllMsg() {
        List<MessageAttrBean> messageAttrBeans = null;
        try {
            messageAttrBeans = mDbUtils.findAll(Selector.from(MessageAttrBean.class));
            if(messageAttrBeans != null){
                return messageAttrBeans;
            }else {
                return null;
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    *//**
     * 根据消息类别获取消息属性
     *
     * @param msgType
     * @return
     *//*
    public MessageAttrBean getMsgByType(String msgType) {
        try {
            return mDbUtils.findFirst(Selector.from(MessageAttrBean.class).where("MSG_TYPE", "=", msgType));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    *//**
     * 删除类别对应的消息属性
     * @param attrBean
     * @return
     *//*
    public boolean deleteMessageAttrBean(MessageAttrBean attrBean){
        boolean flag = false;
        try {
            MessageAttrBean object = getMsgByType(attrBean.getMSG_TYPE());
            if (object != null) {
                LogUtil.e(TAG,"delete MessageAttrBean");
                mDbUtils.delete(attrBean);
            } else {
                LogUtil.e(TAG,attrBean.getMSG_TYPE() + " is not exists");
            }
            flag = true;
        } catch (Exception e) {
            // TODO: handle exception
            flag = false;
        }
        return flag;
    }*/
}

