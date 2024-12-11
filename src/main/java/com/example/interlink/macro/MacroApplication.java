//package com.example.interlink.macro;
//
//import org.springframework.boot.*;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Executor;
//
//@SpringBootApplication
//public class MacroApplication implements CommandLineRunner {
//
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final String targetUrl = "http://metal-load-balancer-300049590.ap-northeast-2.elb.amazonaws.com/";
//
//    @Override
//    public void run(String... args){
//        Executor executor = createExecutor(10);
//        int requestPerThread = 100;
//
//        for(int i=0; i<10; i++){
//            executor.execute(() -> sendRequests(requestPerThread));
//        }
//    }
//
//    private void sendRequests(int count){
//        for (int i=0; i< count; i++){
//            try{
//                String response = restTemplate.getForObject(targetUrl, String.class);
//                System.out.println("Response: " + response);
//            } catch (Exception e){
//                System.err.println("Requests failed: " + e.getMessage());
//            }
//
//        }
//    }
//
//    private Executor createExecutor(int threadCount){
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(threadCount);
//        executor.setMaxPoolSize(threadCount);
//        executor.setQueueCapacity(100);
//        executor.setThreadNamePrefix("MacroThread-");
//        executor.initialize();
//        return executor;
//    }
//
//}
