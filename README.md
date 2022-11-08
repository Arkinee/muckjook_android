# muckjook_android
Muck Jook Android Application
language: Kotlin
Development Design Pattern: Clean Architecture, MVVM, AAC

## ��ȹ
'�԰� ���� �༮��'
���� ģ����� �Ŵ� ������ ã�ƴٴϸ� �򰡸� �ϰ� ���� ���並 �����ϴ� ������ ������ ��ȭ��Ų �����Դϴ�.

## ���� ����
- �ȵ���̵� Clean Architecture ����� ����Ʈ���� ���� ���̵� ������Ʈ
- Kotlin + MVVM + AAC ���� ���͵�

## Android Clean Architecture base structures apply

### App(Presentation Layer)
- base: BaseActivity, BaseFragment, �� Base�� �Ǵ� ����
- di: Dependency Inejction ���� module ���� ���� ��ġ. dagger hilt ���
- view: Activity�� Fragment �� view�� ���õ� ���ϵ��� ��ġ.
- viewmodel: viewModel ���ϵ��� ��ġ
- widget: extension(��Ʋ�� Ȯ�� �Լ�), utils ���� ���� ��ġ

### Domain Layer
- model: Response data class�� ��ġ
- repository: data repository�� ���� ����ü�� �̰Ϳ� ��ġ(interface)
- usecase: ������ ��ɺ��� ����ȭ�� usecase�� ��ġ
- utils: ���� ���� �Լ��� ���� ���� ��ġ

### Data Layer
- db: local db�� ���õ� room���� ������ ��ġ
- mapper: data ����� response data class�� domain ����� response data class�� �ٲ��ִ� ������ ��ġ
- remote: api, response model ���� ������ ��ġ
- repository: datasource�� domain�� repository�� implement ������ ��ġ
- utils: base �� ���� ���� �Լ��� ���� ���� ������ ��ġ

---
#### To-Do List
[ ] view, viewmodel �κ� �и��ϱ�