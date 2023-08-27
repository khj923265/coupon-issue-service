FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/coupon-1.0.jar CouponService.jar
ENV USE_PROFILE prod

ENTRYPOINT ["java", "-Dspring.profiles.active=${USE_PROFILE}", "-jar", "CouponService.jar"]


