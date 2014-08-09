package com.newfivefour.natcher.networking;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

public class RecentPostsService {

    private MessageBusService<RecentPosts, RecentPostsServiceInterface> mService;

    public RecentPostsService() {
        mService = new MessageBusService<RecentPosts, RecentPostsServiceInterface>();
    }

    public void fetch() {
        String url = "https://android-manchester.co.uk/api/rest";

        mService.fetch(
                url,
                RecentPostsServiceInterface.class,
                new MessageBusService.GetResult<RecentPosts, RecentPostsServiceInterface>() {
                    @Override public RecentPosts getResult(RecentPostsServiceInterface service) throws Exception {
                        return service.go(0, 10);
                    }
                },
                new RecentPostsError(),
                new RecentPostsCached(),
                "/post/0/10",
                RecentPosts.class
        );
    }

    public static interface RecentPostsServiceInterface {
        @GET("/post/{start}/{num}")
        RecentPosts go(@Path("start") int start, @Path("num") int num);
    }

    public static class RecentPostsError extends ErrorResponse {}
    public static class RecentPostsCached extends MessageBusService.CachedResponse<RecentPosts> {};

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

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content= content;
            }
        }
    }


}

