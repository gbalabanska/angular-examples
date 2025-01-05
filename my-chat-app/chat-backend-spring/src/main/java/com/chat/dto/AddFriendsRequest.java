package com.chat.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddFriendsRequest {
    private List<Integer> friendsIdtoAddList;

}
