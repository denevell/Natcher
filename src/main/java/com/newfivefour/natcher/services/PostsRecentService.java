package com.newfivefour.natcher.services;

import android.os.Bundle;

import com.newfivefour.natcher.networking.NetworkingMessageBusService;
import com.newfivefour.natcher.networking.ResponseEmpty;
import com.newfivefour.natcher.networking.ResponseError;
import com.newfivefour.natcher.networking.ServerOrCachedResponse;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

public class PostsRecentService {

    private static final String TAG = PostsRecentService.class.getSimpleName();
    private NetworkingMessageBusService<RecentPosts, RecentPostsServiceInterface> mService;

    @SuppressWarnings("unchecked")
    public void fetch(Bundle f) {
        String baseUrl = ServiceUrls.base;

        new NetworkingMessageBusService.Builder<RecentPosts, RecentPostsServiceInterface>()
                .doNotReRequestExistingRequest(f)
                .cacheRequest("/post/0/10")
                .detectEmptyResponse(new NetworkingMessageBusService.IsEmpty<RecentPosts>() {
                                         @Override public boolean isEmpty(RecentPosts res) {
                                             return res.getPosts()==null || res.getPosts().size()==0;
                                         }
                                     },
                        new RecentPostsEmpty())
                .create()
                .fetch(baseUrl,
                        RecentPostsServiceInterface.class,
                        new NetworkingMessageBusService.GetResult<RecentPosts, RecentPostsServiceInterface>() {
                            @Override
                            public RecentPosts getResult(RecentPostsServiceInterface service) throws Exception {
                                return service.go(0, 10);
                            }
                        },
                        new RecentPostsError(),
                        RecentPosts.class
                );
    }

    public static interface RecentPostsServiceInterface {
        @GET("/post/{start}/{num}")
        RecentPosts go(@Path("start") int start, @Path("num") int num);
    }

    public static class RecentPostsError extends ResponseError {}
    public static class RecentPostsEmpty extends ResponseEmpty {}
    public static class RecentPosts extends ServerOrCachedResponse {

        private List<Post> posts;

        public List<Post> getPosts() {
            return posts;
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }

        public static class Post {
            private String content;
            private String subject;

            @Override
            public String toString() {
                return content;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }
        }
    }


}

