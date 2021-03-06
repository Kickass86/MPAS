package turbotec.mpas;


class MessageObject {


    private int MessageID;
    private String MessageTitle;


    private boolean Seen;
    private boolean SendDelivered;
    private boolean SendSeen;
    private String MessageBody;
    private String InsertDate;
    private boolean Critical;


    MessageObject(int MessageID, String MessageTitle, String MessageBody, String InsertDate, boolean Critical, boolean Seen, boolean SendDelivered, boolean SendSeen) {
        this.MessageID = MessageID;
        this.MessageTitle = MessageTitle;
        this.MessageBody = MessageBody;
        this.InsertDate = InsertDate;
        this.Critical = Critical;
        this.Seen = Seen;
        this.SendDelivered = SendDelivered;
        this.SendSeen = SendSeen;

    }


    MessageObject() {

    }

    public void setSendDelivered(boolean sendDelivered) {
        SendDelivered = sendDelivered;
    }

    public boolean isSendSeen() {
        return SendSeen;
    }

    public void setSendSeen(boolean sendSeen) {
        SendSeen = sendSeen;
    }

    public boolean getCritical() {
        return Critical;
    }

    public void setCritical(boolean critical) {
        Critical = critical;
    }

    public boolean isSeen() {
        return Seen;
    }

    public void setSeen(boolean seen) {
        Seen = seen;
    }


    public String getInsertDate() {
        return InsertDate;
    }

    public void setInsertDate(String insertDate) {
        InsertDate = insertDate;
    }

    public String getMessageBody() {
        return MessageBody;
    }

    public void setMessageBody(String messageBody) {
        MessageBody = messageBody;
    }

    public int getMessageID() {
        return MessageID;
    }

    public void setMessageID(int messageID) {
        MessageID = messageID;
    }

//    public String getUserID() {
//        return UserID;
//    }
//
//    public void setUserID(String userID) {
//        UserID = userID;
//    }

    public String getMessageTitle() {
        return MessageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        MessageTitle = messageTitle;
    }

}
