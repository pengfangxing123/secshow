create table win_fragment
(
  id bigint auto_increment comment '主键'
    primary key,
  fra_name varchar(50) not null comment '栏目名称',
  fra_desc varchar(50) null comment '栏目说明',
  content mediumtext null comment '碎片内容',
  operator varchar(50) null comment '操作人',
  is_deleted tinyint(3) unsigned default '0' null comment '删除标记（1 表示删除，0 表示未删除）',
  type tinyint(3) unsigned default '1' null comment '类型 1 页面 2 字符串',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '更新时间'
)
  comment '大赛碎片管理表'
;

create table win_match_award
(
  id bigint auto_increment
    primary key,
  match_id int not null comment '比赛ID',
  brank int UNSIGNED null,
  erank int UNSIGNED null,
  award varchar(100) null comment '奖品',
  award_type tinyint UNSIGNED null comment '奖品类型',
  issue_type tinyint UNSIGNED null,
  valid_status bit default b'1' not null comment '1有效，其他无效',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '修改时间'
)
  comment '比赛奖励表'
;

create table win_match_basic
(
  id bigint auto_increment
    primary key,
  match_owner int UNSIGNED null comment '比赛所有者',
  match_name varchar(100) not null comment '比赛名称',
  start_date date not null comment '比赛开始日期',
  allow_num int default '0' null comment '允许人数 默认0表示不限制',
  end_date date null comment '比赛结束日期',
  match_status tinyint default '1' null comment '比赛状态 1未开始报名、2开始报名、3比赛开始、4比赛结束、-1删除失效',
  type smallint(6) unsigned not null comment '比赛类型 1联赛 2 团队赛',
  apply_start_date date not null comment '参加比赛开始日期',
  apply_end_date date not null comment '参加比赛结束日期',
  curr_num int default '0' null comment '当前参赛人数',
  validate_num tinyint default '0' null comment '有效参赛人数',
  weight int unsigned default '0' null comment '比赛权重',
  summary varchar(500) null comment '大赛说明',
  award_desc varchar(500) null comment '奖励说明',
  rule_desc varchar(500) null comment '规则说明',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '修改时间'
)
  comment '大赛基本表'
;

create table win_match_join
(
  id bigint auto_increment
    primary key,
  user_id BIGINT(20) UNSIGNED not null comment '用户ID',
  match_id int not null comment '比赛ID',
  account_id varchar(50) not null comment '用户账户id',
  join_date date not null comment '加入比赛时间',
  is_deleted tinyint default '0' not null comment '状态 1 表示删除，0 表示未删除',
  match_status int(4) unsigned default '0' null comment '比赛结束状态 1未结束，0已结束',
  trade_times int unsigned default '0' comment '交易次数',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '更新时间',
  constraint uk_user_id_match_id_account_id
  unique (user_id, match_id, account_id)
)
  comment '帐户加入比赛表'
;

create index idx_accountId_status
  on win_match_join (account_id, is_deleted)
;

create index idx_matchId
  on win_match_join (match_id)
;

create table win_match_rule
(
  id bigint auto_increment
    primary key,
  matchId int UNSIGNED null comment '比赛ID',
  `key` varchar(20) null comment '键',
  value varchar(5000) null comment '值',
  valid_status bit default b'1' null comment '1有效，其他无效',
  bina int default '0' null comment '二进制值',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '修改时间'
)
  comment '比赛规则明细表'
;

create index idx_key
  on win_match_rule (`key`)
;

create index idx_matchId
  on win_match_rule (matchId)
;

create table win_match_team_basic
(
  id bigint auto_increment comment '主键id'
    primary key,
  match_id int not null comment '比赛ID',
  master_id int not null comment '老师id',
  team_name varchar(40) not null comment '战队名称',
  declaration varchar(40) not null comment '战队宣言',
  is_deleted tinyint default '0' null comment '1 表示删除，0 表示未删除',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '更新时间',
  constraint uk_team_name
  unique (team_name)
)
  comment '战队基础信息表'
;

create table win_match_team_join
(
  id bigint auto_increment comment '主键id'
    primary key,
  match_id int not null comment '比赛ID',
  team_id int not null comment '战队ID',
  user_id BIGINT(20) UNSIGNED not null comment '用户SSOID',
  account_id varchar(50) not null comment '用户账户id',
  join_date date not null comment '加入时间',
  quit_date date null comment '退出时间',
  join_status tinyint(3) unsigned default '1' null comment '状态，1目前正在团队，0已退出团队',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '更新时间',
  constraint uk_team_id_user_id_account_id
  unique (team_id, user_id, account_id)
)
  comment '战队加入退出情况表'
;

create index idx_teamId
  on win_match_team_join (team_id)
;

create index idx_userId
  on win_match_team_join (user_id)
;

create table win_match_team_member_stat_rank
(
  id bigint auto_increment
    primary key,
  match_id int not null comment '比赛ID',
  team_id bigint default '0' not null comment '团队Id',
  user_id BIGINT(20) UNSIGNED not null comment '用户Id',
  account_id varchar(50) not null comment '用户账户id',
  stat_date date not null comment '排名日期',
  day_yield decimal(19,6) null comment '日收益率',
  day_rank int UNSIGNED null comment '日收益排名',
  last_day_rank int UNSIGNED null comment '上一工作日日排名',
  day_trend tinyint(1) null comment '日排名趋势',
  week_yield decimal(19,6) null comment '周收益率',
  week_rank int UNSIGNED null comment '周收益排名',
  last_week_rank int UNSIGNED null comment '上一工作日周排名',
  week_trend tinyint(1) null comment '周排名趋势',
  month_yield decimal(19,6) null comment '月收益率',
  month_rank int UNSIGNED null comment '月收益排名',
  last_month_rank int UNSIGNED null comment '上一工作日月排名',
  month_trend tinyint(1) null comment '月排名趋势',
  total_yield decimal(19,6) null comment '总收益率',
  total_rank int UNSIGNED null comment '总收益排名',
  last_total_rank int UNSIGNED null comment '上一工作日总排名',
  total_trend tinyint(1) null comment '总排名趋势',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '更新时间',
  constraint uk_team_id_user_id_account_id_stat_date
  unique (team_id, user_id, account_id, stat_date)
)
  comment '团队成员收益排行表'
;

create table win_match_team_member_day_stat_rank
(
  match_id int not null comment '比赛ID',
  team_id bigint default '0' not null comment '团队Id',
  user_id BIGINT(20) UNSIGNED not null comment '用户Id',
  account_id varchar(50) not null comment '用户账户id',
  day_yield decimal(19,6) null comment '日收益率',
  day_rank int UNSIGNED null comment '日收益排名',
  last_day_rank int UNSIGNED null comment '上一工作日日排名',
  day_trend tinyint(1) null comment '日排名趋势',
  week_yield decimal(19,6) null comment '周收益率',
  week_rank int UNSIGNED null comment '周收益排名',
  last_week_rank int UNSIGNED null comment '上一工作日周排名',
  week_trend tinyint(1) null comment '周排名趋势',
  month_yield decimal(19,6) null comment '月收益率',
  month_rank int UNSIGNED null comment '月收益排名',
  last_month_rank int UNSIGNED null comment '上一工作日月排名',
  month_trend tinyint(1) null comment '月排名趋势',
  total_yield decimal(19,6) null comment '总收益率',
  total_rank int UNSIGNED null comment '总收益排名',
  last_total_rank int UNSIGNED null comment '上一工作日总排名',
  total_trend tinyint(1) null comment '总排名趋势',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '更新时间',
  PRIMARY KEY (match_id, team_id, account_id)
)
  comment '团队成员日收益排行表'
;

create index idx_team_id
  on win_match_team_member_stat_rank (team_id)
;

create table win_match_team_stat_rank
(
  id bigint auto_increment comment '主键id'
    primary key,
  match_id int not null comment '比赛ID',
  team_id int not null comment '战队ID',
  member_num int default '0' null comment '战队人数',
  stat_date date not null comment '排名日期',
  day_yield decimal(19,6) null comment '日收益率',
  day_rank int UNSIGNED null comment '日收益排名',
  last_day_rank int UNSIGNED null comment '上一工作日日排名',
  day_trend tinyint(1) null comment '日排名趋势',
  week_yield decimal(19,6) null comment '周收益率',
  week_rank int UNSIGNED null comment '周收益排名',
  last_week_rank int UNSIGNED null comment '上一工作日周排名',
  week_trend tinyint(1) null comment '周排名趋势',
  month_yield decimal(19,6) null comment '月收益率',
  month_rank int UNSIGNED null comment '月收益排名',
  last_month_rank int UNSIGNED null comment '上一工作日月排名',
  month_trend tinyint(1) null comment '月排名趋势',
  total_yield decimal(19,6) null comment '总收益率',
  total_rank int UNSIGNED null comment '总收益排名',
  last_total_rank int UNSIGNED null comment '上一工作日总排名',
  total_trend tinyint(1) null comment '总排名趋势',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '更新时间',
  constraint uk_team_id_stat_date
  unique (team_id, stat_date)
)
  comment '战队收益排行表'
;

create table win_match_team_day_stat_rank
(
  match_id int not null comment '比赛ID',
  team_id int not null comment '战队ID',
  member_num int default '0' null comment '战队人数',
  day_yield decimal(19,6) null comment '日收益率',
  day_rank int UNSIGNED null comment '日收益排名',
  last_day_rank int UNSIGNED null comment '上一工作日日排名',
  day_trend tinyint(1) null comment '日排名趋势',
  week_yield decimal(19,6) null comment '周收益率',
  week_rank int UNSIGNED null comment '周收益排名',
  last_week_rank int UNSIGNED null comment '上一工作日周排名',
  week_trend tinyint(1) null comment '周排名趋势',
  month_yield decimal(19,6) null comment '月收益率',
  month_rank int UNSIGNED null comment '月收益排名',
  last_month_rank int UNSIGNED null comment '上一工作日月排名',
  month_trend tinyint(1) null comment '月排名趋势',
  total_yield decimal(19,6) null comment '总收益率',
  total_rank int UNSIGNED null comment '总收益排名',
  last_total_rank int UNSIGNED null comment '上一工作日总排名',
  total_trend tinyint(1) null comment '总排名趋势',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '更新时间',
  PRIMARY KEY (match_id, team_id)
)
  comment '战队日收益排行表'
;

create table win_match_user_stat_rank
(
  id bigint auto_increment comment '主键id'
    primary key,
  match_id int not null comment '比赛ID',
  user_id BIGINT(20) UNSIGNED not null comment '用户id',
  account_id varchar(50) not null comment '账户总收益',
  stat_date date not null comment '排名日期',
  day_yield decimal(19,6) null comment '日收益率',
  day_rank int UNSIGNED null comment '日收益排名',
  last_day_rank int UNSIGNED null comment '上一工作日日排名',
  day_trend tinyint(1) null comment '日排名趋势',
  week_yield decimal(19,6) null comment '周收益率',
  week_rank int UNSIGNED null comment '周收益排名',
  last_week_rank int UNSIGNED null comment '上一工作日周排名',
  week_trend tinyint(1) null comment '周排名趋势',
  month_yield decimal(19,6) null comment '月收益率',
  month_rank int UNSIGNED null comment '月收益排名',
  last_month_rank int UNSIGNED null comment '上一工作日月排名',
  month_trend tinyint(1) null comment '月排名趋势',
  total_yield decimal(19,6) null comment '总收益率',
  total_rank int UNSIGNED null comment '总收益排名',
  last_total_rank int UNSIGNED null comment '上一工作日总排名',
  total_trend tinyint(1) null comment '总排名趋势',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '更新时间',
  constraint uk_match_id_stat_date_account_id
  unique (match_id, stat_date, account_id)
)
  comment '比赛账户收益和排名'
;

create table win_match_user_day_stat_rank
(
  match_id int not null comment '比赛ID',
  user_id BIGINT(20) UNSIGNED not null comment '用户id',
  account_id varchar(50) not null comment '账户总收益',
  day_yield decimal(19,6) null comment '日收益率',
  day_rank int UNSIGNED null comment '日收益排名',
  last_day_rank int UNSIGNED null comment '上一工作日日排名',
  day_trend tinyint(1) null comment '日排名趋势',
  week_yield decimal(19,6) null comment '周收益率',
  week_rank int UNSIGNED null comment '周收益排名',
  last_week_rank int UNSIGNED null comment '上一工作日周排名',
  week_trend tinyint(1) null comment '周排名趋势',
  month_yield decimal(19,6) null comment '月收益率',
  month_rank int UNSIGNED null comment '月收益排名',
  last_month_rank int UNSIGNED null comment '上一工作日月排名',
  month_trend tinyint(1) null comment '月排名趋势',
  total_yield decimal(19,6) null comment '总收益率',
  total_rank int UNSIGNED null comment '总收益排名',
  last_total_rank int UNSIGNED null comment '上一工作日总排名',
  total_trend tinyint(1) null comment '总排名趋势',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '更新时间',
  PRIMARY KEY (match_id, account_id)
)
  comment '比赛账户日收益和排名'
;

create index idx_stat_date
  on win_match_user_stat_rank (stat_date)
;

create table win_send_red_detail
(
  id bigint auto_increment comment '主键id'
    primary key,
  match_id int UNSIGNED not null comment '比赛ID',
  user_id BIGINT UNSIGNED default '0' null comment '用户Id',
  open_id varchar(50) default '0' null,
  cash int default '0' null comment '红包金额，单位：分',
  phone_no varchar(20) null comment '用户手机号',
  red_type varchar(4) not null comment '红包类型：S分享红包，P抢红包',
  red_status tinyint not null comment '调用状态：0未成功，1已成功',
  err_msg varchar(200) null comment '错误信息',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '修改时间'
)
  comment '红包发放明细表'
;

create table win_stat_config
(
  id bigint auto_increment
    primary key,
  `key` varchar(100) null comment '配置key',
  value varchar(500) null comment 'key对应的value',
  valid bit default b'1' null comment '状态0：false 1:true',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '修改时间'
)
  comment '清算配置表'
;

create table win_trading
(
  id bigint auto_increment
    primary key,
  match_id int not null comment '比赛ID',
  user_id BIGINT(20) UNSIGNED not null comment '用户SSOID',
  account_id varchar(50) not null comment '用户账户id',
  commission_id bigint UNSIGNED null comment '委托id',
  commission_type int UNSIGNED null comment '委托类型 0是买 1是卖 40 委托买入 41 委托卖出',
  commission_amount int not null comment '成交数量',
  commission_price decimal(13,2) not null comment '成交价格',
  trade_cost decimal(13,2) default '0.00' null comment '成交费用',
  from_position decimal(8,2) default '0.00' not null comment '起初持仓',
  to_position decimal(8,2) default '0.00' null comment '确认持仓',
  profit decimal(8,2) default '0.00' null comment '收益',
  stock_code varchar(8) not null comment '股票代码',
  stock_name varchar(10) not null comment '股票名称',
  commission_time datetime null comment '成交时间',
  conclude_time datetime null comment '成交时间',
  position_id bigint UNSIGNED null comment '持仓id',
  action_type tinyint(2) unsigned default '0' null comment '1 建仓 2 加仓 3 减仓 4 清仓',
  commission_status tinyint(2) unsigned default '0' null comment '0 新建 1 已报 2 部撤 3 已撤 4 部成 5 已成 6 废单',
  stock_type varchar(2) default 's' null comment '股票类型',
  market_type varchar(10) default 'cn.sh' null comment '市场类型',
  conclude_price decimal(13,2) default '0.00' null comment '成交金额',
  task_status tinyint default '0' null comment '任务状态',
  is_profit tinyint default '0' null comment '是否盈利，null无盈利,0亏损,1盈利',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '更改时间'
)
  comment '调仓记录表'
;

create index idx_conclude_time_commission_type
  on win_trading (conclude_time, commission_type)
;

create index idx_user_id
  on win_trading (user_id)
;

create table win_match_user
(
  id bigint auto_increment
    primary key,
  passport_id varchar(20) null comment '第三方userId',
  open_id varchar(50) null,
  head_pic varchar(128) null comment '头像',
  nick_name varchar(255) null comment '昵称',
  user_status tinyint default '1' null comment '状态 默认为 1  正常   0删除',
  summary varchar(200) null comment '简介',
  type tinyint default '1' null comment '用户类型',
  phone_no varchar(20) null comment '手机号',
  last_login_time datetime null comment '最近登录时间',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '修改时间'
)
  comment '用户表'
;

create index idx_pid
  on win_match_user (passport_id)
;

create table win_match_user_account
(
  id bigint auto_increment
    primary key,
  user_id BIGINT(20) UNSIGNED not null comment '用户id',
  account_id varchar(50) not null comment '用户账户id',
  init_amount decimal(19,6) null comment '初始资金',
  current_amount decimal(19,6) null comment '当前资金',
  frozen_amount decimal(19,6) null comment '冻结资金',
  last_amount decimal(19,6) null comment '当前资金',
  trade_times int unsigned default '0' comment '交易次数',
  account_status tinyint default '1' null comment '状态 1正常 0停止交易',
  type smallint(6) null comment '账户类型 1普通账户',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '更新时间时间',
  constraint uk_account_id
  unique (account_id)
)
  comment '用户账户关系表'
;


create table win_match_user_account_stat_rank
(
  id int auto_increment comment '主键id'
    primary key,
  account_id varchar(50) not null comment '账户ID',
  user_id BIGINT(20) UNSIGNED not null comment '用户SSOID',
  stat_date date not null comment '清算日期',
  day_yield decimal(19,6) null comment '日收益率',
  day_rank int UNSIGNED null comment '日收益排名',
  last_day_rank int UNSIGNED null comment '上一工作日日排名',
  day_trend tinyint(1) null comment '日排名趋势',
  week_yield decimal(19,6) null comment '周收益率',
  week_rank int UNSIGNED null comment '周收益排名',
  last_week_rank int UNSIGNED null comment '上一工作日周排名',
  week_trend tinyint(1) null comment '周排名趋势',
  month_yield decimal(19,6) null comment '月收益率',
  month_rank int UNSIGNED null comment '月收益排名',
  last_month_rank int UNSIGNED null comment '上一工作日月排名',
  month_trend tinyint(1) null comment '月排名趋势',
  total_yield decimal(19,6) null comment '总收益率',
  total_rank int UNSIGNED null comment '总收益排名',
  last_total_rank int UNSIGNED null comment '上一工作日总排名',
  total_trend tinyint(1) null comment '总排名趋势',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '更新时间时间',
  constraint uk_account_id_stat_date
  unique (account_id, stat_date)
)
  comment '用户账户收益排名表'
;

create index idx_stat_date
  on win_match_user_account_stat_rank (stat_date)
;

create table win_match_user_account_day_stat_rank
(
  account_id varchar(50) not null comment '账户ID' PRIMARY KEY,
  user_id BIGINT(20) UNSIGNED not null comment '用户SSOID',
  day_yield decimal(19,6) null comment '日收益率',
  day_rank int UNSIGNED null comment '日收益排名',
  last_day_rank int UNSIGNED null comment '上一工作日日排名',
  day_trend tinyint(1) null comment '日排名趋势',
  week_yield decimal(19,6) null comment '周收益率',
  week_rank int UNSIGNED null comment '周收益排名',
  last_week_rank int UNSIGNED null comment '上一工作日周排名',
  week_trend tinyint(1) null comment '周排名趋势',
  month_yield decimal(19,6) null comment '月收益率',
  month_rank int UNSIGNED null comment '月收益排名',
  last_month_rank int UNSIGNED null comment '上一工作日月排名',
  month_trend tinyint(1) null comment '月排名趋势',
  total_yield decimal(19,6) null comment '总收益率',
  total_rank int UNSIGNED null comment '总收益排名',
  last_total_rank int UNSIGNED null comment '上一工作日总排名',
  total_trend tinyint(1) null comment '总排名趋势',
  gmt_create datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  gmt_modified datetime default CURRENT_TIMESTAMP not null on UPDATE current_timestamp comment '更新时间时间'
)
  comment '用户账户日收益排名表'
;