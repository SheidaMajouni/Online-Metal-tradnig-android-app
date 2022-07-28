package com.damavandit.apps.chair.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.damavandit.apps.chair.dbModels.DeliveryPoint;
import com.damavandit.apps.chair.dbModels.MeasurementUnit;
import com.damavandit.apps.chair.dbModels.OrderStatus;
import com.damavandit.apps.chair.dbModels.ProductGroupMeasurementUnit;
import com.damavandit.apps.chair.models.MessageDetail;
import com.damavandit.apps.chair.models.Messages;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_MEASUREMENT_UNIT =
            "CREATE TABLE measurementUnit " +
                    "( measurementUnitId INTEGER PRIMARY KEY, measurementUnitName TEXT )";

    private static final String CREATE_TABLE_ORDER_STATUS =
            "CREATE TABLE orderStatus " +
                    "( orderStatusId INTEGER PRIMARY KEY, orderStatusName TEXT )";

    private static final String CREATE_TABLE_DELIVERY_POINT =
            "CREATE TABLE deliveryPoint " +
                    "( deliveryPointId INTEGER PRIMARY KEY, deliveryPointName TEXT )";

    private static final String CREATE_TABLE_PRODUCT_GROUP_MEASUREMENT_UNIT =
            "CREATE TABLE productGroupMeasurementUnit " +
                    "( productGroupId INTEGER , measurementGroupId INTEGER )";

    private static final String CREATE_TABLE_MESSAGE =
            "CREATE TABLE messageTable " +
                    "( messageId INTEGER PRIMARY KEY, messageTitle TEXT ," +
                    "messageStatusId INTEGER, messageStatus TEXT , messageDate TEXT)";

    private static final String CREATE_TABLE_MESSAGE_DETAIL =
            "CREATE TABLE messageDetail " +
                    "( messageDetailId INTEGER PRIMARY KEY, messageId INTEGER ," +
                    "contentQuestion TEXT, contentAnswer TEXT , questionNumber INTEGER," +
                    "answerNumber INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, "chair_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEASUREMENT_UNIT);
        db.execSQL(CREATE_TABLE_ORDER_STATUS);
        db.execSQL(CREATE_TABLE_DELIVERY_POINT);
        db.execSQL(CREATE_TABLE_PRODUCT_GROUP_MEASUREMENT_UNIT);
        db.execSQL(CREATE_TABLE_MESSAGE);
        db.execSQL(CREATE_TABLE_MESSAGE_DETAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "measurementUnit");
        db.execSQL("DROP TABLE IF EXISTS " + "orderStatus");
        db.execSQL("DROP TABLE IF EXISTS " + "deliveryPointId");
        db.execSQL("DROP TABLE IF EXISTS " + "productGroupMeasurementUnit");
        db.execSQL("DROP TABLE IF EXISTS " + "messageTable");
        db.execSQL("DROP TABLE IF EXISTS " + "messageDetail");

        onCreate(db);
    }

    //----------------------------------measurement unit db functions-----------------------------------
    public void insertMeasurementUnit(MeasurementUnit measurementUnit) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("measurementUnitId", measurementUnit.getMeasurementUnitId());
        values.put("measurementUnitName", measurementUnit.getMeasurementUnitName());

        db.insert("measurementUnit", null, values);
    }

    public MeasurementUnit getMeasurementById(int measurementId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM measurementUnit WHERE measurementUnitId = " + measurementId;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        MeasurementUnit measurementUnit = new MeasurementUnit();
        measurementUnit.setMeasurementUnitId(c.getInt(c.getColumnIndex("measurementUnitId")));
        measurementUnit.setMeasurementUnitName((c.getString(c.getColumnIndex("measurementUnitName"))));

        return measurementUnit;
    }

    public List<MeasurementUnit> getAllToDos() {
        List<MeasurementUnit> measurementUnitList = new ArrayList<MeasurementUnit>();
        String selectQuery = "SELECT  * FROM measurementUnit";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                MeasurementUnit measurementUnit = new MeasurementUnit();
                measurementUnit.setMeasurementUnitId(c.getInt(c.getColumnIndex("measurementUnitId")));
                measurementUnit.setMeasurementUnitName((c.getString(c.getColumnIndex("measurementUnitName"))));

                measurementUnitList.add(measurementUnit);
            } while (c.moveToNext());
        }

        return measurementUnitList;
    }

    public int getMeasurementUnitCount() {
        String selectQuery = "SELECT  * FROM measurementUnit";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        return c.getCount();
    }

    public void deleteAllMeasurementUnit() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from measurementUnit");
    }

//----------------------------------order status db functions---------------------------------------

    public void insertOrderStatus(OrderStatus orderStatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("orderStatusId", orderStatus.getOrderStatusId());
        values.put("orderStatusName", orderStatus.getOrderStatusName());

        db.insert("orderStatus", null, values);
    }

    public OrderStatus getOrderStatusById(int orderStatusId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM orderStatus WHERE orderStatusId = " + orderStatusId;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatusId(c.getInt(c.getColumnIndex("orderStatusId")));
        orderStatus.setOrderStatusName((c.getString(c.getColumnIndex("orderStatusName"))));

        return orderStatus;
    }

    public List<OrderStatus> getAllOrderStatus() {
        List<OrderStatus> orderStatusList = new ArrayList<OrderStatus>();
        String selectQuery = "SELECT  * FROM orderStatus";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setOrderStatusId(c.getInt(c.getColumnIndex("orderStatusId")));
                orderStatus.setOrderStatusName((c.getString(c.getColumnIndex("orderStatusName"))));

                orderStatusList.add(orderStatus);
            } while (c.moveToNext());
        }

        return orderStatusList;
    }

    public int getOrderStatusCount() {
        String selectQuery = "SELECT  * FROM orderStatus";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        return c.getCount();
    }

    public void deleteAllOrderStatus() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from orderStatus");
    }

//----------------------------------delivery point db functions-------------------------------------

    public long insertDeliveryPoint(DeliveryPoint deliveryPoint) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("deliveryPointId", deliveryPoint.getDeliveryPointId());
        values.put("deliveryPointName", deliveryPoint.getDeliveryPointName());

        return db.insert("deliveryPoint", null, values);
    }

    public DeliveryPoint getDeliveryPointById(int deliveryPointId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM deliveryPoint WHERE deliveryPointId = " + deliveryPointId;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        DeliveryPoint deliveryPoint = new DeliveryPoint();
        deliveryPoint.setDeliveryPointId(c.getInt(c.getColumnIndex("deliveryPointId")));
        deliveryPoint.setDeliveryPointName((c.getString(c.getColumnIndex("deliveryPointName"))));

        return deliveryPoint;
    }


    public DeliveryPoint getDeliveryPointByName(String deliveryPointName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM deliveryPoint WHERE deliveryPointName = " + deliveryPointName;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        DeliveryPoint deliveryPoint = new DeliveryPoint();
        deliveryPoint.setDeliveryPointId(c.getInt(c.getColumnIndex("deliveryPointId")));
        deliveryPoint.setDeliveryPointName((c.getString(c.getColumnIndex("deliveryPointName"))));

        return deliveryPoint;
    }

    public List<DeliveryPoint> getAllDeliveryPoint() {
        List<DeliveryPoint> deliveryPointList = new ArrayList<DeliveryPoint>();
        String selectQuery = "SELECT  * FROM deliveryPoint";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                DeliveryPoint deliveryPoint = new DeliveryPoint();
                deliveryPoint.setDeliveryPointId(c.getInt(c.getColumnIndex("deliveryPointId")));
                deliveryPoint.setDeliveryPointName((c.getString(c.getColumnIndex("deliveryPointName"))));

                deliveryPointList.add(deliveryPoint);
            } while (c.moveToNext());
        }

        return deliveryPointList;
    }

    public int getDeliveryPointCount() {
        String selectQuery = "SELECT  * FROM deliveryPoint";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        return c.getCount();
    }

    public void deleteAllDeliveryPoint() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from deliveryPoint");
    }

//-----------------------------productGroupManufacturerUnit db functions----------------------------

    public void insertProductGroupManufacturerUnit(ProductGroupMeasurementUnit productGroupMeasurementUnit) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("productGroupId", productGroupMeasurementUnit.getProductGroupId());
        values.put("measurementGroupId", productGroupMeasurementUnit.getMeasurementGroupId());

        db.insert("productGroupMeasurementUnit", null, values);
    }

    public List<Integer> getMeasurementGroupIdsByProductGroupId(int productGroupId) {

        List<Integer> measurementGroupIdList = new ArrayList<Integer>();
        String selectQuery = "SELECT  * FROM productGroupMeasurementUnit WHERE productGroupId = " + productGroupId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                int id;
                id = c.getInt(c.getColumnIndex("measurementGroupId"));

                measurementGroupIdList.add(id);
            } while (c.moveToNext());
        }

        return measurementGroupIdList;
    }

    public int getProductGroupManufacturerUnitCount() {
        String selectQuery = "SELECT * FROM productGroupMeasurementUnit";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        return c.getCount();
    }

    public void deleteAllProductGroupMeasurementUnit() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from productGroupMeasurementUnit");
    }

    //--------------------------------------messagesTable db function-----------------------------------
    public void insertMessage(Messages message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("messageTitle", message.getMessageTitle());
        values.put("messageStatusId", message.getMessageStatusId());
        values.put("messageStatus", message.getMessageStatus());
        values.put("messageDate", message.getMessageDate());

        db.insert("messageTable", null, values);
    }

    public List<Messages> getAllMessages() {
        List<Messages> messageList = new ArrayList<Messages>();
        String selectQuery = "SELECT  * FROM messageTable";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Messages message = new Messages();
                message.setMessageId(c.getInt(c.getColumnIndex("messageId")));
                message.setMessageTitle(c.getString(c.getColumnIndex("messageTitle")));
                message.setMessageStatusId(c.getInt(c.getColumnIndex("messageStatusId")));
                message.setMessageStatus(c.getString(c.getColumnIndex("messageStatus")));
                message.setMessageDate(c.getString(c.getColumnIndex("messageDate")));

                messageList.add(message);
            } while (c.moveToNext());
        }

        return messageList;
    }

    public int getLastMessageAddedId() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM messageTable WHERE messageId = (SELECT MAX(messageId) FROM messageTable)";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        int maxID = (c.getInt(c.getColumnIndex("messageId")));

        return maxID;
    }

    public Messages getMessageById(int messageId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM messageTable WHERE messageId =" + messageId;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Messages message = new Messages();
        message.setMessageId(c.getInt(c.getColumnIndex("messageId")));
        message.setMessageTitle(c.getString(c.getColumnIndex("messageTitle")));
        message.setMessageStatusId(c.getInt(c.getColumnIndex("messageStatusId")));
        message.setMessageStatus(c.getString(c.getColumnIndex("messageStatus")));
        message.setMessageDate(c.getString(c.getColumnIndex("messageDate")));

        return message;

    }

    //--------------------------------------messagesTable db function-----------------------------------
    public void insertMessageDetail(MessageDetail messageDetail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("messageId", messageDetail.getMessageId());
        values.put("contentQuestion", messageDetail.getContentQuestion());
        values.put("contentAnswer", messageDetail.getContentAnswer());
        values.put("questionNumber", messageDetail.getQuestionNumber());
        values.put("answerNumber", messageDetail.getAnswerNumber());

        db.insert("messageDetail", null, values);
    }

    public List<MessageDetail> getAllMessageDetail(int messageId) {
        List<MessageDetail> messageDetailList = new ArrayList<MessageDetail>();
        String selectQuery = "SELECT  * FROM messageDetail WHERE messageId =" + messageId + " ORDER BY answerNumber ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                MessageDetail messageDetail = new MessageDetail();
                messageDetail.setMessageDetailId(c.getInt(c.getColumnIndex("messageDetailId")));
                messageDetail.setMessageId(c.getInt(c.getColumnIndex("messageId")));
                messageDetail.setContentQuestion(c.getString(c.getColumnIndex("contentQuestion")));
                messageDetail.setContentAnswer(c.getString(c.getColumnIndex("contentAnswer")));
                messageDetail.setQuestionNumber(c.getInt(c.getColumnIndex("questionNumber")));
                messageDetail.setAnswerNumber(c.getInt(c.getColumnIndex("answerNumber")));

                messageDetailList.add(messageDetail);
            } while (c.moveToNext());
        }

        return messageDetailList;
    }

    public int getMaxQuestionNumberById(int messageId) {

        String selectQuery = "SELECT MAX(questionNumber) FROM messageDetail GROUP BY messageId , questionNumber HAVING messageId =" + messageId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        MessageDetail messageDetail = new MessageDetail();
        messageDetail.setQuestionNumber(c.getInt(0));
        return messageDetail.getQuestionNumber();
    }

    //--------------------------------------- closing database------------------------------------------
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}