# muckjook_android
Muck Jook Android Application
language: Kotlin
Development Design Pattern: Clean Architecture, MVVM, AAC

## 기획
'먹고 죽은 녀석들'
동네 친구들과 매달 맛집을 찾아다니며 평가를 하고 서로 리뷰를 공유하는 과정을 앱으로 승화시킨 서비스입니다.

## 개발 목적
- 안드로이드 Clean Architecture 기반의 소프트웨어 개발 사이드 프로젝트
- Kotlin + MVVM + AAC 개인 스터디

## Android Clean Architecture base structures apply

### App(Presentation Layer)
- base: BaseActivity, BaseFragment, 등 Base가 되는 파일
- di: Dependency Inejction 관련 module 파일 등이 위치. dagger hilt 사용
- view: Activity와 Fragment 등 view에 관련된 파일들이 위치.
- viewmodel: viewModel 파일들이 위치
- widget: extension(코틀린 확장 함수), utils 등의 파일 위치

### Domain Layer
- model: Response data class가 위치
- repository: data repository의 실제 구현체가 이것에 위치(interface)
- usecase: 각각의 기능별로 세분화된 usecase가 위치
- utils: 자주 쓰는 함수나 변수 등이 위치

### Data Layer
- db: local db에 관련된 room등의 파일이 위치
- mapper: data 모듈의 response data class를 domain 모듈의 response data class로 바꿔주는 파일이 위치
- remote: api, response model 등의 파일이 위치
- repository: datasource와 domain의 repository의 implement 파일이 위치
- utils: base 등 자주 쓰는 함수나 변수 등의 파일이 위치

---
#### To-Do List
[ ] view, viewmodel 부분 분리하기