-- 유저 (test, admin)
INSERT INTO tbUser (ID, Pass, Email, Name, Role, Status) VALUES
('test',  '$2a$10$URjrapyw/a.PaBRGconFNe2W15RN30s5C5vftcpjA5MeY1nXeAvoG', 'test@example.com',  '테스트유저', 'ROLE_USER',  'ACTIVE'),
('admin', '$2a$10$URjrapyw/a.PaBRGconFNe2W15RN30s5C5vftcpjA5MeY1nXeAvoG', 'admin@example.com', '관리자',     'ROLE_ADMIN', 'ACTIVE');

-- tbBook (10권)
INSERT INTO tbBook (Isbn, Title, Author, Publisher, ImageURL, PublishDate) VALUES
('9788936434001', '어린왕자', '앙투안 드 생텍쥐페리', '민음사', 'https://picsum.photos/seed/book1/80/110', '2007-01-01'),
('9788936434002', '해리포터와 마법사의 돌', 'J.K. 롤링', '문학수첩', 'https://picsum.photos/seed/book2/80/110', '1999-11-01'),
('9788936434003', '나니아 연대기', 'C.S. 루이스', '시공사', 'https://picsum.photos/seed/book3/80/110', '2001-05-01'),
('9788936434004', '샬롯의 거미줄', 'E.B. 화이트', '사파리', 'https://picsum.photos/seed/book4/80/110', '2011-03-01'),
('9788936434005', '빨간머리 앤', 'L.M. 몽고메리', '시공사', 'https://picsum.photos/seed/book5/80/110', '2000-06-01'),
('9788936434006', '모모', '미하엘 엔데', '비룡소', 'https://picsum.photos/seed/book6/80/110', '1999-09-01'),
('9788936434007', '이상한 나라의 앨리스', '루이스 캐럴', '민음사', 'https://picsum.photos/seed/book7/80/110', '2010-04-01'),
('9788936434008', '갈매기의 꿈', '리처드 바크', '현문미디어', 'https://picsum.photos/seed/book8/80/110', '2002-07-01'),
('9788936434009', '피터팬', 'J.M. 배리', '시공사', 'https://picsum.photos/seed/book9/80/110', '2003-02-01'),
('9788936434010', '톰 소여의 모험', '마크 트웨인', '민음사', 'https://picsum.photos/seed/book10/80/110', '2001-08-01');

-- tbDiary (test 유저 10개)
INSERT INTO tbDiary (Idx_User, Idx_Book, StartDate, EndDate, Rating, Favorite, MemoTitle, MemoContent, Status) VALUES
(1,1,'2024-01-01','2024-01-10',5.0,TRUE,'어린왕자 완독','가장 중요한 것은 눈에 보이지 않아.','DONE'),
(1,2,'2024-01-15',NULL,NULL,FALSE,NULL,NULL,'READING'),
(1,3,'2024-02-01','2024-02-15',4.0,TRUE,'나니아 완독','상상력이 풍부한 세계관이 인상적이었다.','DONE'),
(1,4,NULL,NULL,NULL,FALSE,NULL,NULL,'WANT'),
(1,5,'2024-03-01','2024-03-10',4.5,TRUE,'빨간머리 앤 완독','앤의 긍정적인 에너지가 좋았다.','DONE'),
(1,6,NULL,NULL,NULL,TRUE,NULL,NULL,'WANT'),
(1,7,'2024-04-01','2024-04-07',3.5,FALSE,'앨리스 완독','기묘하고 신기한 세계였다.','DONE'),
(1,8,'2024-05-01',NULL,NULL,FALSE,NULL,NULL,'READING'),
(1,9,'2024-05-15','2024-05-22',4.0,FALSE,'피터팬 완독','어른이 되기 싫었던 기억이 떠올랐다.','DONE'),
(1,10,NULL,NULL,NULL,TRUE,NULL,NULL,'WANT');

-- tbHighlight (test 유저 10개)
INSERT INTO tbHighlight (Idx_Diary, PageNumber, HighlightText) VALUES
(1, 21, '가장 중요한 것은 눈에 보이지 않아.'),
(1, 45, '사막이 아름다운 것은 어딘가에 샘을 숨기고 있기 때문이야.'),
(3, 44, '용기란 두려움이 없는 것이 아니라 더 중요한 것이 있다는 판단이다.'),
(3, 88, '언제나 봄이 오듯 희망은 반드시 찾아온다.'),
(5, 32, '내일은 또 다른 날이야.'),
(5, 67, '상상력은 지식보다 중요하다.'),
(7, 15, '진정한 모험은 마음속에서 시작된다.'),
(7, 72, '꿈꾸는 자만이 새로운 세계를 발견한다.'),
(9, 28, '어른들은 숫자를 좋아한다.'),
(9, 53, '별을 바라보는 것만으로도 행복할 수 있어.');

-- tbAILog (test 유저 10개)
INSERT INTO tbAILog (Idx_User, Idx_Book, PromptSummary, Result) VALUES
(1, 1, '어린왕자 줄거리 요약 요청', '{"summary": "어린 왕자가 여러 별을 여행하며 삶의 의미를 찾는 이야기입니다."}'),
(1, 2, '해리포터 줄거리 요약 요청', '{"summary": "마법사 해리포터가 호그와트에서 겪는 모험 이야기입니다."}'),
(1, 3, '나니아 연대기 줄거리 요약 요청', '{"summary": "옷장을 통해 나니아 왕국으로 들어간 아이들의 모험입니다."}'),
(1, 4, '책 추천 요청', '{"recommendations": [{"title": "데미안", "author": "헤르만 헤세"}]}'),
(1, 5, '책 추천 요청', '{"recommendations": [{"title": "어린왕자", "author": "생텍쥐페리"}]}'),
(1, 6, '모모 줄거리 요약 요청', '{"summary": "시간 도둑들로부터 시간을 되찾는 소녀 모모의 이야기입니다."}'),
(1, 7, '앨리스 줄거리 요약 요청', '{"summary": "이상한 나라에서 앨리스가 겪는 신기한 모험 이야기입니다."}'),
(1, 8, '월간 독서 리포트 요청', '{"report": "이번 달 5권을 읽으셨습니다. 훌륭한 독서량이에요!"}'),
(1, 9, '책 추천 요청', '{"recommendations": [{"title": "동물농장", "author": "조지 오웰"}]}'),
(1, 10, '월간 독서 리포트 요청', '{"report": "완독 3권, 읽는 중 2권으로 꾸준히 독서하고 계십니다!"}');