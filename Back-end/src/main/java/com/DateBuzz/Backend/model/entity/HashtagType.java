package com.DateBuzz.Backend.model.entity;

public enum HashtagType {
    VIBE, ACTIVITY, CUSTOM;

    public static HashtagType returnHashtag(String type){
        if(type.equals("VIBE")) return HashtagType.VIBE;
        if(type.equals("ACTIVITY")) return HashtagType.VIBE;
        return HashtagType.CUSTOM;
    }
}
