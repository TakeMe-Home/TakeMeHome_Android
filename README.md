# 나를 집으로 데려가줘!(Take Me Home)

## 서비스 소개

- 배달 애플리케이션을 벤치마킹하여 고객, 점주, 라이더를 하나의 애플리케이션으로 통합한 플랫폼
- 라이더로부터 배달지까지의 최단거리 목록을 보여줌으로써 편의성 향상

</br >

## 주요 기능

### 고객

- 특정 가게에 메뉴 주문을 할 수 있습니다.
  - 메뉴 주문시 해당 가게에 알림이 갑니다.
  - 메뉴 주문시 거리에 따라 배달료를 알려줍니다.
- 내가 주문한 목록을 확인할 수 있습니다.

### 라이더

- 전체 콜 목록 조회할 수 있습니다.
  - 콜 목록 조회시 현재 라이더 위치에서 목적지까지의 거리순으로 확인할 수 있습니다.
- 콜 접수 목록을 확인할 수 있습니다.
- 콜 접수 목록의 상태를 변경을 할 수 있습니다.
  - ex) 배차 완료,픽업, 배달 완료

### 점주

- 자신의 가게를 등록할 수 있습니다.
  - 설정한 가게에 메뉴 추가, 삭제, 품절과 같은 상태 변경을 할 수 있습니다.
- 가게에 주문이 들어오면 주문을 수락 및 거절 할 수 있습니다.
  - 수락 시 소요 시간을 등록하여 고객에게 알려줍니다.
  - 거절시 거절 사유와 함께 고객에게 알려줍니다.
- 라이더에게 배차 요청을 할 수 있습니다.
  - 배창 요청시 음식이 만들어지는 소요 시간을 알려줍니다.
  - 기간 및 주문 상태 별 주문 목록을 확인할 수 있습니다.
  - 배차 요청시 라이더에게 알림이 갑니다.

</br >

## 기술 스택

- Kotlin
- Retrofit2 2.7.0
- Firebase Cloud Messaging
- Androidx
  - recyclerview 1.1.0

## 테스트 환경

- 갤럭시 S20
- 갤럭시 A8 20184

## 시연영상

https://youtu.be/rcG6G_uI5Hg
