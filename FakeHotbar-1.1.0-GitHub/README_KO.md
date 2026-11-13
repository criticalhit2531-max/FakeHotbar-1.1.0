# Fake Hotbar 1.1.0

Minecraft Java 1.21.11 / Fabric / Java 21

## 포함된 기능

- 기본 표시 순서: `4 5 3 1 6 7 2 8 9`
- `O` 키로 설정 GUI 열기
- GUI에서 두 위치를 선택해 순서 교환
- Reset / Save / Cancel
- `config/fakehotbar.json` 자동 저장
- HUD 핫바 아이템 표시 위치 변경
- 인벤토리 화면의 플레이어 핫바 슬롯 좌표 변경
- 실제 PlayerInventory 인덱스와 ScreenHandler 슬롯 ID는 변경하지 않음
- GitHub Actions에서 JAR 자동 빌드

## GitHub에서 JAR 빌드하기

1. 이 ZIP을 압축 해제합니다.
2. GitHub에서 새 빈 저장소(repository)를 만듭니다.
3. 압축 해제한 폴더 안의 파일과 폴더를 저장소 최상위에 업로드합니다.
   - `.github` 폴더도 반드시 포함해야 합니다.
   - `build.gradle`, `settings.gradle`, `gradle.properties`가 최상위에 있어야 합니다.
4. `Actions` 탭을 엽니다.
5. `Build Fake Hotbar` 작업이 실행될 때까지 기다립니다.
6. 작업이 초록색 완료 표시가 되면 작업 상세 페이지의 `Artifacts`에서
   `FakeHotbar-1.1.0`을 다운로드합니다.
7. Artifact ZIP 안의 `fakehotbar-1.1.0.jar`를 사용합니다.

## 로컬 빌드

Java 21과 Gradle 9.6.0이 설치되어 있다면 프로젝트 폴더에서:

    gradle --no-daemon clean build

완성 JAR:

    build/libs/fakehotbar-1.1.0.jar

## 조작

- 게임 안에서 `O`: Fake Hotbar 설정 GUI 열기
- 숫자 버튼 두 개를 차례로 클릭: 두 표시 위치 교환
- Reset: 기본 순서로 초기화
- Save: 저장 및 적용
- Cancel: 저장하지 않고 닫기

## 중요: 테스트 상태

이 저장소는 Minecraft 1.21.11 Yarn `build.1` API를 기준으로 작성되었습니다.
현재 실행 환경에는 Gradle이 설치되어 있지 않아 여기서 실제 Gradle 빌드나
Minecraft 클라이언트 실행 테스트를 수행하지 못했습니다. 따라서 이 파일을
"컴파일 및 게임 테스트까지 통과한 완성 JAR"이라고 보장하지 않습니다.

특히 InGameHud의 선택 프레임 렌더링 Mixin은 1.21.11의 실제 실행 시
Mixin 대상 호출과 일치하는지 확인해야 합니다. GitHub Actions가 빌드에
실패하면 Actions의 첫 번째 오류 로그를 확인해 수정해야 합니다.

Lunar Client에서 사용할 수 있는지도 실제 클라이언트에서 확인해야 합니다.
