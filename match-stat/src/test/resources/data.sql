/*
集成测试用例数据
两个比赛，比赛1为联赛，比赛2为团队赛
 */
INSERT INTO win_match_basic (match_name, match_pic, start_date, end_date, match_status, type, apply_start_date, apply_end_date, rule_config)
VALUES ('测试比赛1', '123,jpg', '2018-10-08', '2018-10-31', 3, 1, '2018-10-01', '2018-10-31','{"personal":{"reward":{"total":1000,"ratio":"5:3:2"}},"team":{"win_team_reward":{"total":1000,"ratio":"5:3:2"},"constitute_reward":{"min_amount":200,"rule":{"step_num":1,"step_amount":1},"max_amount":500},"rule":{"constitute_num":2000,"start_stat_ratio":20,"change_team_interval":100},"win_team_member_reward":{"total":1000,"ratio":"5:3:2"}}}');
INSERT INTO win_match_basic (match_name, match_pic, start_date, end_date, match_status, type, apply_start_date, apply_end_date, rule_config)
VALUES ('测试比赛2', '123.jpg', '2018-09-27', '2018-10-31', 3, 2, '2018-10-01', '2018-10-31','{"personal":{"reward": {"total":1000, "ratio": "5:3:2"} }, "team":{"rule":{"change_team_interval": 20, "constitute_num": 40, "start_stat_ratio": 50 }, "constitute_reward":{"min_amount": 200, "max_amount": 500, "rule": {"step_num": 1, "step_amount": 1 } }, "win_team_reward": {"total":1000, "ratio": "5:3:2"}, "win_team_member_reward": {"total":1000, "ratio": "5:3:2"} } }');
-- 201个用户，用于测试分页拉取数据有效性
insert into win_match_user_account(`user_id`, `account_id`) values (100,'win_100');
insert into win_match_user_account(`user_id`, `account_id`) values (1000,'win_1000');
insert into win_match_user_account(`user_id`, `account_id`) values (10000,'win_10000');
insert into win_match_user_account(`user_id`, `account_id`) values (100000,'win_100000');
insert into win_match_user_account(`user_id`, `account_id`) values (100001,'win_100001');
insert into win_match_user_account(`user_id`, `account_id`) values (100002,'win_100002');
insert into win_match_user_account(`user_id`, `account_id`) values (100003,'win_100003');
insert into win_match_user_account(`user_id`, `account_id`) values (100004,'win_100004');
insert into win_match_user_account(`user_id`, `account_id`) values (100005,'win_100005');
insert into win_match_user_account(`user_id`, `account_id`) values (100006,'win_100006');
insert into win_match_user_account(`user_id`, `account_id`) values (100007,'win_100007');
insert into win_match_user_account(`user_id`, `account_id`) values (100008,'win_100008');
insert into win_match_user_account(`user_id`, `account_id`) values (100009,'win_100009');
insert into win_match_user_account(`user_id`, `account_id`) values (10001,'win_10001');
insert into win_match_user_account(`user_id`, `account_id`) values (100010,'win_100010');
insert into win_match_user_account(`user_id`, `account_id`) values (100011,'win_100011');
insert into win_match_user_account(`user_id`, `account_id`) values (100012,'win_100012');
insert into win_match_user_account(`user_id`, `account_id`) values (100013,'win_100013');
insert into win_match_user_account(`user_id`, `account_id`) values (100014,'win_100014');
insert into win_match_user_account(`user_id`, `account_id`) values (100015,'win_100015');
insert into win_match_user_account(`user_id`, `account_id`) values (100016,'win_100016');
insert into win_match_user_account(`user_id`, `account_id`) values (100017,'win_100017');
insert into win_match_user_account(`user_id`, `account_id`) values (100018,'win_100018');
insert into win_match_user_account(`user_id`, `account_id`) values (100019,'win_100019');
insert into win_match_user_account(`user_id`, `account_id`) values (10002,'win_10002');
insert into win_match_user_account(`user_id`, `account_id`) values (100020,'win_100020');
insert into win_match_user_account(`user_id`, `account_id`) values (100021,'win_100021');
insert into win_match_user_account(`user_id`, `account_id`) values (100022,'win_100022');
insert into win_match_user_account(`user_id`, `account_id`) values (100023,'win_100023');
insert into win_match_user_account(`user_id`, `account_id`) values (100024,'win_100024');
insert into win_match_user_account(`user_id`, `account_id`) values (100025,'win_100025');
insert into win_match_user_account(`user_id`, `account_id`) values (100026,'win_100026');
insert into win_match_user_account(`user_id`, `account_id`) values (100027,'win_100027');
insert into win_match_user_account(`user_id`, `account_id`) values (100028,'win_100028');
insert into win_match_user_account(`user_id`, `account_id`) values (100029,'win_100029');
insert into win_match_user_account(`user_id`, `account_id`) values (10003,'win_10003');
insert into win_match_user_account(`user_id`, `account_id`) values (100030,'win_100030');
insert into win_match_user_account(`user_id`, `account_id`) values (100031,'win_100031');
insert into win_match_user_account(`user_id`, `account_id`) values (100032,'win_100032');
insert into win_match_user_account(`user_id`, `account_id`) values (100033,'win_100033');
insert into win_match_user_account(`user_id`, `account_id`) values (100034,'win_100034');
insert into win_match_user_account(`user_id`, `account_id`) values (100035,'win_100035');
insert into win_match_user_account(`user_id`, `account_id`) values (100036,'win_100036');
insert into win_match_user_account(`user_id`, `account_id`) values (100037,'win_100037');
insert into win_match_user_account(`user_id`, `account_id`) values (100038,'win_100038');
insert into win_match_user_account(`user_id`, `account_id`) values (100039,'win_100039');
insert into win_match_user_account(`user_id`, `account_id`) values (10004,'win_10004');
insert into win_match_user_account(`user_id`, `account_id`) values (100040,'win_100040');
insert into win_match_user_account(`user_id`, `account_id`) values (100041,'win_100041');
insert into win_match_user_account(`user_id`, `account_id`) values (100042,'win_100042');
insert into win_match_user_account(`user_id`, `account_id`) values (100043,'win_100043');
insert into win_match_user_account(`user_id`, `account_id`) values (100044,'win_100044');
insert into win_match_user_account(`user_id`, `account_id`) values (100045,'win_100045');
insert into win_match_user_account(`user_id`, `account_id`) values (100046,'win_100046');
insert into win_match_user_account(`user_id`, `account_id`) values (100047,'win_100047');
insert into win_match_user_account(`user_id`, `account_id`) values (100048,'win_100048');
insert into win_match_user_account(`user_id`, `account_id`) values (100049,'win_100049');
insert into win_match_user_account(`user_id`, `account_id`) values (10005,'win_10005');
insert into win_match_user_account(`user_id`, `account_id`) values (100050,'win_100050');
insert into win_match_user_account(`user_id`, `account_id`) values (100051,'win_100051');
insert into win_match_user_account(`user_id`, `account_id`) values (100052,'win_100052');
insert into win_match_user_account(`user_id`, `account_id`) values (100053,'win_100053');
insert into win_match_user_account(`user_id`, `account_id`) values (100054,'win_100054');
insert into win_match_user_account(`user_id`, `account_id`) values (100055,'win_100055');
insert into win_match_user_account(`user_id`, `account_id`) values (100056,'win_100056');
insert into win_match_user_account(`user_id`, `account_id`) values (100057,'win_100057');
insert into win_match_user_account(`user_id`, `account_id`) values (100058,'win_100058');
insert into win_match_user_account(`user_id`, `account_id`) values (100059,'win_100059');
insert into win_match_user_account(`user_id`, `account_id`) values (10006,'win_10006');
insert into win_match_user_account(`user_id`, `account_id`) values (100060,'win_100060');
insert into win_match_user_account(`user_id`, `account_id`) values (100061,'win_100061');
insert into win_match_user_account(`user_id`, `account_id`) values (100062,'win_100062');
insert into win_match_user_account(`user_id`, `account_id`) values (100063,'win_100063');
insert into win_match_user_account(`user_id`, `account_id`) values (100064,'win_100064');
insert into win_match_user_account(`user_id`, `account_id`) values (100065,'win_100065');
insert into win_match_user_account(`user_id`, `account_id`) values (100066,'win_100066');
insert into win_match_user_account(`user_id`, `account_id`) values (100067,'win_100067');
insert into win_match_user_account(`user_id`, `account_id`) values (100068,'win_100068');
insert into win_match_user_account(`user_id`, `account_id`) values (100069,'win_100069');
insert into win_match_user_account(`user_id`, `account_id`) values (10007,'win_10007');
insert into win_match_user_account(`user_id`, `account_id`) values (100070,'win_100070');
insert into win_match_user_account(`user_id`, `account_id`) values (100071,'win_100071');
insert into win_match_user_account(`user_id`, `account_id`) values (100072,'win_100072');
insert into win_match_user_account(`user_id`, `account_id`) values (100073,'win_100073');
insert into win_match_user_account(`user_id`, `account_id`) values (100074,'win_100074');
insert into win_match_user_account(`user_id`, `account_id`) values (100075,'win_100075');
insert into win_match_user_account(`user_id`, `account_id`) values (100076,'win_100076');
insert into win_match_user_account(`user_id`, `account_id`) values (100077,'win_100077');
insert into win_match_user_account(`user_id`, `account_id`) values (100078,'win_100078');
insert into win_match_user_account(`user_id`, `account_id`) values (100079,'win_100079');
insert into win_match_user_account(`user_id`, `account_id`) values (10008,'win_10008');
insert into win_match_user_account(`user_id`, `account_id`) values (100080,'win_100080');
insert into win_match_user_account(`user_id`, `account_id`) values (100081,'win_100081');
insert into win_match_user_account(`user_id`, `account_id`) values (100082,'win_100082');
insert into win_match_user_account(`user_id`, `account_id`) values (100083,'win_100083');
insert into win_match_user_account(`user_id`, `account_id`) values (100084,'win_100084');
insert into win_match_user_account(`user_id`, `account_id`) values (100085,'win_100085');
insert into win_match_user_account(`user_id`, `account_id`) values (100086,'win_100086');
insert into win_match_user_account(`user_id`, `account_id`) values (100087,'win_100087');
insert into win_match_user_account(`user_id`, `account_id`) values (100088,'win_100088');
insert into win_match_user_account(`user_id`, `account_id`) values (100089,'win_100089');
insert into win_match_user_account(`user_id`, `account_id`) values (10009,'win_10009');
insert into win_match_user_account(`user_id`, `account_id`) values (100090,'win_100090');
insert into win_match_user_account(`user_id`, `account_id`) values (100091,'win_100091');
insert into win_match_user_account(`user_id`, `account_id`) values (100092,'win_100092');
insert into win_match_user_account(`user_id`, `account_id`) values (100093,'win_100093');
insert into win_match_user_account(`user_id`, `account_id`) values (100094,'win_100094');
insert into win_match_user_account(`user_id`, `account_id`) values (100095,'win_100095');
insert into win_match_user_account(`user_id`, `account_id`) values (100096,'win_100096');
insert into win_match_user_account(`user_id`, `account_id`) values (100097,'win_100097');
insert into win_match_user_account(`user_id`, `account_id`) values (100098,'win_100098');
insert into win_match_user_account(`user_id`, `account_id`) values (100099,'win_100099');
insert into win_match_user_account(`user_id`, `account_id`) values (1001,'win_1001');
insert into win_match_user_account(`user_id`, `account_id`) values (10010,'win_10010');
insert into win_match_user_account(`user_id`, `account_id`) values (100100,'win_100100');
insert into win_match_user_account(`user_id`, `account_id`) values (100101,'win_100101');
insert into win_match_user_account(`user_id`, `account_id`) values (100102,'win_100102');
insert into win_match_user_account(`user_id`, `account_id`) values (100103,'win_100103');
insert into win_match_user_account(`user_id`, `account_id`) values (100104,'win_100104');
insert into win_match_user_account(`user_id`, `account_id`) values (100105,'win_100105');
insert into win_match_user_account(`user_id`, `account_id`) values (100106,'win_100106');
insert into win_match_user_account(`user_id`, `account_id`) values (100107,'win_100107');
insert into win_match_user_account(`user_id`, `account_id`) values (100108,'win_100108');
insert into win_match_user_account(`user_id`, `account_id`) values (100109,'win_100109');
insert into win_match_user_account(`user_id`, `account_id`) values (10011,'win_10011');
insert into win_match_user_account(`user_id`, `account_id`) values (100110,'win_100110');
insert into win_match_user_account(`user_id`, `account_id`) values (100111,'win_100111');
insert into win_match_user_account(`user_id`, `account_id`) values (100112,'win_100112');
insert into win_match_user_account(`user_id`, `account_id`) values (100113,'win_100113');
insert into win_match_user_account(`user_id`, `account_id`) values (100114,'win_100114');
insert into win_match_user_account(`user_id`, `account_id`) values (100115,'win_100115');
insert into win_match_user_account(`user_id`, `account_id`) values (100116,'win_100116');
insert into win_match_user_account(`user_id`, `account_id`) values (100117,'win_100117');
insert into win_match_user_account(`user_id`, `account_id`) values (100118,'win_100118');
insert into win_match_user_account(`user_id`, `account_id`) values (100119,'win_100119');
insert into win_match_user_account(`user_id`, `account_id`) values (10012,'win_10012');
insert into win_match_user_account(`user_id`, `account_id`) values (100120,'win_100120');
insert into win_match_user_account(`user_id`, `account_id`) values (100121,'win_100121');
insert into win_match_user_account(`user_id`, `account_id`) values (100122,'win_100122');
insert into win_match_user_account(`user_id`, `account_id`) values (100123,'win_100123');
insert into win_match_user_account(`user_id`, `account_id`) values (100124,'win_100124');
insert into win_match_user_account(`user_id`, `account_id`) values (100125,'win_100125');
insert into win_match_user_account(`user_id`, `account_id`) values (100126,'win_100126');
insert into win_match_user_account(`user_id`, `account_id`) values (100127,'win_100127');
insert into win_match_user_account(`user_id`, `account_id`) values (100128,'win_100128');
insert into win_match_user_account(`user_id`, `account_id`) values (100129,'win_100129');
insert into win_match_user_account(`user_id`, `account_id`) values (10013,'win_10013');
insert into win_match_user_account(`user_id`, `account_id`) values (100130,'win_100130');
insert into win_match_user_account(`user_id`, `account_id`) values (100131,'win_100131');
insert into win_match_user_account(`user_id`, `account_id`) values (100132,'win_100132');
insert into win_match_user_account(`user_id`, `account_id`) values (100133,'win_100133');
insert into win_match_user_account(`user_id`, `account_id`) values (100134,'win_100134');
insert into win_match_user_account(`user_id`, `account_id`) values (100135,'win_100135');
insert into win_match_user_account(`user_id`, `account_id`) values (100136,'win_100136');
insert into win_match_user_account(`user_id`, `account_id`) values (100137,'win_100137');
insert into win_match_user_account(`user_id`, `account_id`) values (100138,'win_100138');
insert into win_match_user_account(`user_id`, `account_id`) values (100139,'win_100139');
insert into win_match_user_account(`user_id`, `account_id`) values (10014,'win_10014');
insert into win_match_user_account(`user_id`, `account_id`) values (100140,'win_100140');
insert into win_match_user_account(`user_id`, `account_id`) values (100141,'win_100141');
insert into win_match_user_account(`user_id`, `account_id`) values (100142,'win_100142');
insert into win_match_user_account(`user_id`, `account_id`) values (100143,'win_100143');
insert into win_match_user_account(`user_id`, `account_id`) values (100144,'win_100144');
insert into win_match_user_account(`user_id`, `account_id`) values (100145,'win_100145');
insert into win_match_user_account(`user_id`, `account_id`) values (100146,'win_100146');
insert into win_match_user_account(`user_id`, `account_id`) values (100147,'win_100147');
insert into win_match_user_account(`user_id`, `account_id`) values (100148,'win_100148');
insert into win_match_user_account(`user_id`, `account_id`) values (100149,'win_100149');
insert into win_match_user_account(`user_id`, `account_id`) values (10015,'win_10015');
insert into win_match_user_account(`user_id`, `account_id`) values (100150,'win_100150');
insert into win_match_user_account(`user_id`, `account_id`) values (100151,'win_100151');
insert into win_match_user_account(`user_id`, `account_id`) values (100152,'win_100152');
insert into win_match_user_account(`user_id`, `account_id`) values (100153,'win_100153');
insert into win_match_user_account(`user_id`, `account_id`) values (100154,'win_100154');
insert into win_match_user_account(`user_id`, `account_id`) values (100155,'win_100155');
insert into win_match_user_account(`user_id`, `account_id`) values (100156,'win_100156');
insert into win_match_user_account(`user_id`, `account_id`) values (100157,'win_100157');
insert into win_match_user_account(`user_id`, `account_id`) values (100158,'win_100158');
insert into win_match_user_account(`user_id`, `account_id`) values (100159,'win_100159');
insert into win_match_user_account(`user_id`, `account_id`) values (10016,'win_10016');
insert into win_match_user_account(`user_id`, `account_id`) values (100160,'win_100160');
insert into win_match_user_account(`user_id`, `account_id`) values (100161,'win_100161');
insert into win_match_user_account(`user_id`, `account_id`) values (100162,'win_100162');
insert into win_match_user_account(`user_id`, `account_id`) values (100163,'win_100163');
insert into win_match_user_account(`user_id`, `account_id`) values (100164,'win_100164');
insert into win_match_user_account(`user_id`, `account_id`) values (100165,'win_100165');
insert into win_match_user_account(`user_id`, `account_id`) values (100166,'win_100166');
insert into win_match_user_account(`user_id`, `account_id`) values (100167,'win_100167');
insert into win_match_user_account(`user_id`, `account_id`) values (100168,'win_100168');
insert into win_match_user_account(`user_id`, `account_id`) values (100169,'win_100169');
insert into win_match_user_account(`user_id`, `account_id`) values (10017,'win_10017');
insert into win_match_user_account(`user_id`, `account_id`) values (100170,'win_100170');
insert into win_match_user_account(`user_id`, `account_id`) values (100171,'win_100171');
insert into win_match_user_account(`user_id`, `account_id`) values (100172,'win_100172');
insert into win_match_user_account(`user_id`, `account_id`) values (100173,'win_100173');
insert into win_match_user_account(`user_id`, `account_id`) values (100174,'win_100174');
insert into win_match_user_account(`user_id`, `account_id`) values (100175,'win_100175');
insert into win_match_user_account(`user_id`, `account_id`) values (100176,'win_100176');
insert into win_match_user_account(`user_id`, `account_id`) values (100177,'win_100177');
insert into win_match_user_account(`user_id`, `account_id`) values (100178,'win_100178');
insert into win_match_user_account(`user_id`, `account_id`) values (100179,'win_100179');

-- 所有用户随机参赛，所有参赛参赛人员设置交易次数为3
INSERT INTO win_match_join (user_id, match_id, account_id, join_date, match_status, trade_times)
  SELECT user_id, 1, account_id, DATEADD('DAY',floor(rand()*10), TIMESTAMP'2018-09-26 10:00:00'),1, 3 FROM win_match_user_account;
INSERT INTO win_match_join (user_id, match_id, account_id, join_date, match_status, trade_times)
  SELECT user_id, 2, account_id, DATEADD('DAY',floor(rand()*10), TIMESTAMP'2018-09-26 10:00:00'),1, 3 FROM win_match_user_account;
-- 指定一个特殊用户用于结果验证
UPDATE win_match_join SET join_date = '2018-10-09 10:00:00' WHERE match_id=1 AND user_id=100179;
UPDATE win_match_join SET join_date = '2018-09-28 10:00:00' WHERE match_id=2 AND user_id=100179;

-- 添加3个团队
INSERT INTO win_match_team_basic(match_id, master_id, team_name, declaration) VALUES
  (2,1,'战队1','战队宣言1'),(2,2,'战队2','战队宣言2'),(2,3,'战队3','战队宣言3');
-- 前2个团队随机40人参加以保证能正常进入清算，最后一个团队少于40人，用于验证
INSERT INTO win_match_team_join (match_id, team_id, user_id, account_id, join_date)
  SELECT 2, 1, user_id, account_id, '2018-09-28 10:00:00' FROM win_match_user_account ORDER BY user_id LIMIT 0,40;
INSERT INTO win_match_team_join (match_id, team_id, user_id, account_id, join_date)
  SELECT 2, 2, user_id, account_id, '2018-09-28 10:00:00' FROM win_match_user_account ORDER BY user_id LIMIT 40,40;
INSERT INTO win_match_team_join (match_id, team_id, user_id, account_id, join_date)
  SELECT 2, 3, user_id, account_id, DATEADD('DAY',floor(rand()*10), TIMESTAMP'2018-10-08 10:00:00') FROM win_match_user_account ORDER BY user_id LIMIT 80,39;

-- 添加09-28一天的清算数据用户于验证数据
-- INSERT INTO win_match_user_account_stat_rank(account_id,user_id, stat_date, day_yield, week_yield, month_yield, total_yield) VALUES
--  ('win_100179','1000179', '2018-09-28', 0.12, 0.12,0.12,0.12);
-- 添加交易记录用于交易次数统计
INSERT INTO win_trading (user_id, account_id, commission_id, commission_type, commission_amount, commission_price, trade_cost, from_position, to_position, profit, stock_code, stock_name, commission_time, conclude_time, position_id, action_type, commission_status, stock_type, market_type, conclude_price, task_status, is_profit) VALUES ('100179', 'win_100179', null, null, 100, 100.20, 0.00, 0.00, 100.00, 0.00, '601360', '三六零', null, '2018-10-09 11:15:10', null, 0, 0, 's', 'cn.sh', 0.00, 0, 0);
INSERT INTO win_trading (user_id, account_id, commission_id, commission_type, commission_amount, commission_price, trade_cost, from_position, to_position, profit, stock_code, stock_name, commission_time, conclude_time, position_id, action_type, commission_status, stock_type, market_type, conclude_price, task_status, is_profit) VALUES ('100179', 'win_100179', null, null, 100, 10.10, 0.00, 0.00, 100.00, 0.00, '000001', '平安银行', null, '2018-10-26 11:24:46', null, 0, 0, 's', 'cn.sh', 0.00, 0, 0);
INSERT INTO win_trading (user_id, account_id, commission_id, commission_type, commission_amount, commission_price, trade_cost, from_position, to_position, profit, stock_code, stock_name, commission_time, conclude_time, position_id, action_type, commission_status, stock_type, market_type, conclude_price, task_status, is_profit) VALUES ('100179', 'win_100179', null, null, 100, 12.10, 0.00, 0.00, 100.00, 0.00, '601500', '通用股份', null, '2018-09-25 10:42:22', null, 0, 0, 's', 'cn.sh', 0.00, 0, 0);
-- 清算配置表数据
INSERT INTO win_stat_config (`key`, value, valid) VALUES ('companyId', '30', true);
INSERT INTO win_stat_config (`key`, value, valid) VALUES ('exchangeId', 'SSAS', true);
