package com.grpc.store.demo;

import com.google.type.Date;
import com.grpc.store.demo.StoreProviderGrpc;
import com.grpc.store.demo.ProductId;
import com.grpc.store.demo.Product;
import com.grpc.store.demo.ProductByName;
import com.grpc.store.demo.Order;
import com.grpc.store.demo.Stock;
import com.grpc.store.demo.StockByProduct;
import io.grpc.stub.StreamObserver;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Random;

public class StoreService extends StoreProviderGrpc.StoreProviderImplBase {

    private static final Logger log = LoggerFactory.getLogger(StoreService.class.getName());

    @Override
    public void unaryStreamingGetProductById(ProductId request, StreamObserver<Product> responseObserver) {
        Random random = new Random();
        Product response = Product.newBuilder()
                .setProductId(request.getProductId())
                .setProductName("Product " + RandomStringUtils.randomAlphabetic(10))
                .setProductDescription(RandomStringUtils.randomAlphabetic(10))
                .setProductPrice(random.nextDouble())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void serverSideStreamingGetProductByName(ProductByName request, StreamObserver<Product> responseObserver) {
        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            Product response = Product.newBuilder()
                    .setProductId(RandomStringUtils.randomAlphanumeric(5))
                    .setProductName(request.getProductName() + " " + RandomStringUtils.randomAlphabetic(10))
                    .setProductDescription(RandomStringUtils.randomAlphabetic(10))
                    .setProductPrice(random.nextDouble())
                    .build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Product> clientSideStreamingGetCreateOrder(final StreamObserver<Order> responseObserver) {
       return new StreamObserver<>() {
           int count = 0;
           double price = 0.0;

           @Override
           public void onNext(Product product) {
               count++;
               price += product.getProductPrice();
           }

           @Override
           public void onError(Throwable throwable) {
               log.error("error occurred while processing the request. err: {}", throwable.getMessage());
           }

           @Override
           public void onCompleted() {
               LocalDate currentDate = LocalDate.now();
               Date date = Date.newBuilder()
                       .setDay(currentDate.getDayOfMonth())
                       .setMonth(currentDate.getMonthValue())
                       .setYear(currentDate.getYear())
                       .build();

              Order order = Order.newBuilder()
                      .setOrderId(RandomStringUtils.randomAlphanumeric(5))
                      .setOrderStatus("Pending")
                      .setOrderDate(date)
                      .setTotalAmount(price)
                      .setItemsNumber(count)
                      .build();
              responseObserver.onNext(order);
              responseObserver.onCompleted();
           }
       };
    }

    @Override
    public StreamObserver<Stock> bidirectionalStreamingUpdateStock(StreamObserver<StockByProduct> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(Stock stock) {
                log.info("Stock received for product: {}, quantity: {}", stock.getProductId(), stock.getItemsNumber());
                Random random = new Random();
                StockByProduct stockByProduct = StockByProduct.newBuilder()
                        .setProductId(stock.getProductId())
                        .setProductName(RandomStringUtils.randomAlphabetic(10))
                        .setProductDescription(RandomStringUtils.randomAlphabetic(10))
                        .setProductPrice(random.nextDouble())
                        .setItemsNumber(stock.getItemsNumber())
                        .build();
                responseObserver.onNext(stockByProduct);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("error occurred while processing the request. err: {}", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
