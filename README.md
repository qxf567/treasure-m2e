# treasure-m2e
这个项目是从mysql的binlog日志中读取业务相关部分然后同步到elastisearch中

开启binlog日志,并设置为row模式

show variables like 'log_%'; 查看系统变量

show master logs;--查看所有binlog日志列表

show master status;--查看master状态，即最后(最新)一个binlog日志的编号名称，及其最后一个操作事件pos结束点(Position)值

flush logs; -- 刷新log日志，自此刻开始产生一个新编号的binlog日志文件

reset master; -- 重置(清空)所有binlog日志

/usr/local/mysql/bin/mysqlbinlog /usr/local/mysql/data/mysql-bin.000013 --查看binlog
mysqlbinlog  mysqlbin-log.000021  --base64-output=decode-rows -v|more

show binlog events; -- 查询第一个(最早)的binlog日志
show binlog events in 'mysql-bin.000021' from 8224 limit 2,10\G;

Mysql binlog日志有三种格式，分别为Statement,MiXED,以及ROW
binlog_format = MIXED //binlog日志格式
log_bin =目录/mysql-bin.log //binlog日志名
expire_logs_days = 7 //binlog过期清理时间
max_binlog_size 100m //binlog每个日志文件大小 


