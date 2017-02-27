package turbotec.mpas;


class MessageObject {


    private int MessageID;
    private String UserID;
    private String MessageTitle;


    private String MessageBody;
    private String InsertDate;
    private int Delivered;

    public MessageObject(int MessageID, String UserID, String MessageTitle, String MessageBody, String InsertDate, int Delivered) {
        this.MessageID = MessageID;
        this.UserID = UserID;
        this.MessageTitle = MessageTitle;
        this.MessageBody = MessageBody;
        this.InsertDate = InsertDate;
        this.Delivered = Delivered;

    }

    public MessageObject() {

    }

    public int getDelivered() {
        return Delivered;
    }

    public void setDelivered(int delivered) {
        Delivered = delivered;
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

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getMessageTitle() {
        return MessageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        MessageTitle = messageTitle;
    }

}
