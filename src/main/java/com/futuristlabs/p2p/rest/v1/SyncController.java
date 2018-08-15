package com.futuristlabs.p2p.rest.v1;

import com.futuristlabs.p2p.func.sync.DataSync;
import com.futuristlabs.p2p.func.sync.ReferenceSyncData;
import com.futuristlabs.p2p.func.sync.UserSyncData;
import com.futuristlabs.p2p.utils.Utils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/v1/sync")
public class SyncController {

    private DataSync dataSync;

    @Autowired
    public SyncController(DataSync dataSync) {
        this.dataSync = dataSync;
    }

    @RequestMapping(value = "/reference", method = GET)
    @ResponseBody
    public ReferenceSyncData syncReferenceData(
            @RequestHeader(value = "If-Modified-Since", required = false) String modifiedSinceStr
    ) {
        final DateTime modifiedSince = Utils.parseDate(modifiedSinceStr);
        return dataSync.getReferenceData(modifiedSince);
    }

    @RequestMapping(value = "/user", method = GET)
    @ResponseBody
    public UserSyncData syncUserData(
            @RequestHeader(value = "If-Modified-Since", required = false) String modifiedSinceStr,
            @RequestParam(value = "userId") UUID userId
    ) {
        final DateTime modifiedSince = Utils.parseDate(modifiedSinceStr);
        return dataSync.getUserData(modifiedSince, userId);
    }

    @RequestMapping(value = "/user", method = POST)
    @ResponseBody
    public UserSyncData updateData(
            @RequestParam(value = "userId") UUID userId,
            @RequestBody UserSyncData userSyncData
    ) {
        dataSync.updateUserData(userSyncData, userId);
        return userSyncData;
    }

}

