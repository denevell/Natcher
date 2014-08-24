package com.newfivefour.natcher.services;

import android.os.Bundle;

import com.newfivefour.natcher.networking.NetworkingMessageBusService;
import com.newfivefour.natcher.networking.ResponseError;

import java.util.ArrayList;
import java.util.List;

import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.PUT;

public class PostAddService {

    private static final String TAG = PostAddService.class.getSimpleName();
    private NetworkingMessageBusService<com.newfivefour.natcher.models.PostAdded, PostAddInterface> mService;

    @SuppressWarnings("unchecked")
    public void fetch(Bundle f, final String authKey, final String content) {
        String baseUrl = ServiceUrls.base;

        final PostAdd postAdd = new PostAdd("-", content, null, null);
        new NetworkingMessageBusService.Builder<PostAdd, PostAddInterface>()
                .dontRerequestExistingRequest(f)
                .fetch(baseUrl,
                        PostAddInterface.class,
                        new NetworkingMessageBusService.GetResult<com.newfivefour.natcher.models.PostAdded, PostAddInterface>() {
                            @Override
                            public com.newfivefour.natcher.models.PostAdded getResult(PostAddInterface service) throws Exception {
                                return service.go("bdc2c587-2d88-4b79-a5ad-e0047b219337", postAdd);
                            }
                        },
                        new PostAddError(),
                        PostAdd.class
                );
    }

    public PostAddService() {
        mService = new NetworkingMessageBusService<com.newfivefour.natcher.models.PostAdded, PostAddInterface>();
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

