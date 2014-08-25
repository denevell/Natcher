package com.newfivefour.natcher.services;

import android.os.Bundle;

import com.newfivefour.natcher.models.PostAdded;
import com.newfivefour.natcher.networking.NetworkingMessageBusService;
import com.newfivefour.natcher.networking.NetworkingServiceDelegate;
import com.newfivefour.natcher.networking.ResponseError;
import com.newfivefour.natcher.networking.NetworkingMessageBusService.Builder;

import java.util.ArrayList;
import java.util.List;

import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.PUT;

public class PostAddService implements NetworkingServiceDelegate {

    private static final String TAG = PostAddService.class.getSimpleName();
    private NetworkingMessageBusService<PostAdded, PostAddInterface> mService;

    @SuppressWarnings("unchecked")
    public PostAddService(Bundle f) {
        mService = new Builder<PostAdded, PostAddInterface>()
                        .dontRerequestExistingRequest(f)
                        .create();
    }

    @SuppressWarnings("unchecked")
    public void fetch(final String authKey, final String subject, final String content) {
        String baseUrl = ServiceUrls.base;

        final PostAdd postAdd = new PostAdd(subject, content, null, null);
        mService.fetch(baseUrl,
                PostAddInterface.class,
                new NetworkingMessageBusService.GetResult<com.newfivefour.natcher.models.PostAdded, PostAddInterface>() {
                    @Override
                    public com.newfivefour.natcher.models.PostAdded getResult(PostAddInterface service) throws Exception {
                        return service.go("bdc2c587-2d88-4b79-a5ad-e0047b219337", postAdd);
                    }
                },
                new PostAddError(),
                PostAdded.class
        );
    }

    @Override
    public NetworkingMessageBusService getNetworkingService() {
        return mService;
    }


    public static interface PostAddInterface {
        @PUT("/post/addthread")
        com.newfivefour.natcher.models.PostAdded go(@Header("AuthKey") String authKey, @Body PostAdd post);
    }

    public static class PostAddError extends ResponseError {}
    public static class PostAdd {
        public PostAdd(String subject, String content, String threadId, List<String> tags) {
            this.subject = subject;
            this.content = content;
            this.threadId = threadId;
            this.tags = tags;
        }
        public String subject;
        public String content;
        public String threadId;
        public List<String> tags = new ArrayList<>();
    }

}

