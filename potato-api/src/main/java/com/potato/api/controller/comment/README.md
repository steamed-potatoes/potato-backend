## 프로젝트 및 요구사항 정리

- 이 프로젝트는 교내 동아리와 관련된 신입 모집, 행사 등 일정들을 관리할 수 있는 웹 플랫폼입니다.

### 유저 권한의 종류

먼저, 저희 서비스는 유저의 권한이 크게 4가지로 볼 수 있습니다.

- 일반 회원
- 동아리 소속의 회원
- 동아리 소속의 동아리 관리자
- 서비스 관리자

### 게시물의 종류

먼저 게시물에는 여러 종류가 있습니다.

- 학사 일정 등 관리자가 업로드하는 게시물
- 신입 회원 모집, 동아리 행사 등 그룹 관리자가 업로드하는 게시물

cf) 차후 스터디 그룹등 개인이 올릴 수 있는 기능 등 확장성을 고려한 상황입니다.

### 댓글 작성

- 관리자가 업로드한 게시물, 그룹에서 업로드한 게시물에 댓글을 작성할 수 있는 기능.
- 로그인한 유저만이 댓글을 작성할 수 있습니다.
- 댓글에 대댓글을 작성할 수 있으며 (2depth 까지만 댓글을 작성할 수 있게 제한해둔 상황이며, 이 정책은 차후 변경될 수 있다는 점을 고려해서 설계하였습니다)

### 댓글 조회
- 작성한 댓글은 익명성을 보장합니다. (다른 사람들이 봤을때 누가 작성한 댓글인지 알 수 없음)
- 댓글, 대댓글에 로그인한 유저가 좋아요를 누를 수 있어서, 유저가 댓글을 조회할때, 해당 댓글을 자신이 좋아요를 눌렀는지 확인하는 기능이 필요합니다.
- 댓글을 삭제하면, 기존의 계층 구조를 깨트리지 않도록 "삭제된 댓글입니다"라고 표시되어야 합니다.

### BoardCommentController.java

[https://github.com/steamed-potatoes/potato-backend/blob/develop/potato-api/src/main/java/com/potato/api/controller/comment/BoardCommentController.java](https://github.com/steamed-potatoes/potato-backend/blob/develop/potato-api/src/main/java/com/potato/api/controller/comment/BoardCommentController.java)

### BoardCommentService.java

[https://github.com/steamed-potatoes/potato-backend/blob/develop/potato-api/src/main/java/com/potato/api/service/comment/BoardCommentService.java](https://github.com/steamed-potatoes/potato-backend/blob/develop/potato-api/src/main/java/com/potato/api/service/comment/BoardCommentService.java)

### BoardComment.java

[https://github.com/steamed-potatoes/potato-backend/blob/08ece7b95c827386b6bc2ea3086e6344290424f0/potato-domain/src/main/java/com/potato/domain/domain/comment/BoardComment.java#L76](https://github.com/steamed-potatoes/potato-backend/blob/08ece7b95c827386b6bc2ea3086e6344290424f0/potato-domain/src/main/java/com/potato/domain/domain/comment/BoardComment.java#L76)

### BoardCommentRepositoryCustomImpl.java

[https://github.com/steamed-potatoes/potato-backend/blob/develop/potato-domain/src/main/java/com/potato/domain/domain/comment/repository/BoardCommentRepositoryCustomImpl.java](https://github.com/steamed-potatoes/potato-backend/blob/develop/potato-domain/src/main/java/com/potato/domain/domain/comment/repository/BoardCommentRepositoryCustomImpl.java)
