package com.quicktutorialz.nio;

import com.mawashi.nio.jetty.ReactiveJ;

/**
 * Created by E076103 on 09-01-2019.
 */
public class MainApplication {

  public static void main(String[] args) throws Exception {
    //run the embedded jetty server
    new ReactiveJ().port(8383)
        .endpoints(new com.quicktutorialz.nio.endpoints.v1.ToDoEndPoints())
        .endpoints(new com.quicktutorialz.nio.endpoints.v2.ToDoEndPoints())
        .start();
  }

}
