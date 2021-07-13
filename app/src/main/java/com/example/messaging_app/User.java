package com.example.messaging_app;

import com.google.firebase.firestore.FieldValue;

public class User {
    public String Name;
    public String ImgUrl;
    public String thumbImgUrl;
    public String deviceToken;
    public String status;
    public String OnlineStatus;
    public String Uid;

    User(){
       //default
    }

    User(String Name,String ImgUrl, String thumbImgUrl, String Uid){
        this.Name=Name ;
        this.ImgUrl=ImgUrl ;
        this.thumbImgUrl=thumbImgUrl;
        this.deviceToken="";
        this.Uid=Uid;
        this.status="Hi, I am using Messaging App";
        this.OnlineStatus= "";
    }

}
