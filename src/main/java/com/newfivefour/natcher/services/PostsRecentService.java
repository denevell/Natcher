package com.newfivefour.natcher.services;

import android.os.Bundle;

import com.newfivefour.natcher.networking.ErrorResponse;
import com.newfivefour.natcher.networking.NetworkingMessageBusService;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

public class PostsRecentService {

    private static final String TAG = PostsRecentService.class.getSimpleName();
    private NetworkingMessageBusService<RecentPosts, RecentPostsServiceInterface> mService;

    @SuppressWarnings("unchecked")
    public void fetch(Bundle f) {
        String baseUrl = "https://android-manchester.co.uk/api/rest";

        new NetworkingMessageBusService.Builder<RecentPosts, RecentPostsServiceInterface>()
                .dontRerequestExistingRequest(f)
                .cacheRequest("/post/0/10", new RecentPostsCached())
                .fetch(baseUrl,
                        RecentPostsServiceInterface.class,
                        new NetworkingMessageBusService.GetResult<RecentPosts, RecentPostsServiceInterface>() {
                            @Override public RecentPosts getResult(RecentPostsServiceInterface service) throws Exception {
                                return service.go(0, 10);
                            }
                        },
                        new RecentPostsError(),
                        RecentPosts.class
                );
    }

    public PostsRecentService() {
        mService = new NetworkingMessageBusService<RecentPosts, RecentPostsServiceInterface>();
    }

    public static interface RecentPostsServiceInterface {
        @GET("/post/{start}/{num}")
        RecentPosts go(@Path("start") int start, @Path("num") int num);
    }

    public static class RecentPostsError extends ErrorResponse {}
    public static class RecentPostsCached extends NetworkingMessageBusService.CachedResponse<RecentPosts> {};
    public static class RecentPosts {

        private List<Post> posts;

        public List<Post> getPosts() {
            return posts;
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }

        public static class Post {
            private String content;

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
        }
    }


}

