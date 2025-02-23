DROP TABLE IF EXISTS "SYS_PAGE";
CREATE TABLE "SYS_PAGE"
(
    "ID" BIGINT IDENTITY(1, 1) NOT NULL,
    "CODE" VARCHAR(50) NOT NULL,
    "NAME" VARCHAR(100) NOT NULL,
    "CONTENT" CLOB,
    "REMARK" VARCHAR(255) DEFAULT '',
    "STATUS" INT DEFAULT 1 NOT NULL,
    "DELETED" INT DEFAULT 0 NOT NULL,
    "CREATE_BY" BIGINT,
    "CREATE_DATE" TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP(),
    "UPDATE_BY" BIGINT,
    "UPDATE_DATE" TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP(),
    NOT CLUSTER PRIMARY KEY("ID"),
    UNIQUE("CODE")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "SYS_PAGE" IS '页面表';
COMMENT ON COLUMN "SYS_PAGE"."CODE" IS '编码';
COMMENT ON COLUMN "SYS_PAGE"."CONTENT" IS '页面json';
COMMENT ON COLUMN "SYS_PAGE"."CREATE_BY" IS '创建人';
COMMENT ON COLUMN "SYS_PAGE"."CREATE_DATE" IS '创建时间';
COMMENT ON COLUMN "SYS_PAGE"."DELETED" IS '是否删除标记 - 0:正常,1:已删除';
COMMENT ON COLUMN "SYS_PAGE"."ID" IS '主键';
COMMENT ON COLUMN "SYS_PAGE"."NAME" IS '名称';
COMMENT ON COLUMN "SYS_PAGE"."REMARK" IS '备注';
COMMENT ON COLUMN "SYS_PAGE"."STATUS" IS '状态 - 0:停用,1:启用';
COMMENT ON COLUMN "SYS_PAGE"."UPDATE_BY" IS '最后更新人';
COMMENT ON COLUMN "SYS_PAGE"."UPDATE_DATE" IS '最后更新时间';


CREATE UNIQUE  INDEX "INDEX3414266073800" ON "SYS_PAGE"("ID" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

DROP TABLE IF EXISTS "SYS_ROUTE";
CREATE TABLE "SYS_ROUTE"
(
    "ID" BIGINT IDENTITY(1, 1) NOT NULL,
    "CODE" VARCHAR(100) NOT NULL,
    "NAME" VARCHAR(100) NOT NULL,
    "HOST" VARCHAR(255) DEFAULT '',
    "PATH" VARCHAR(255) DEFAULT '',
    "PARENT_MENU_ID" BIGINT,
    "CACHEABLE" BIT DEFAULT '0',
    "COMPATIBILITY" BIT DEFAULT '0',
    "ICON" VARCHAR(255) NOT NULL,
    "SORT" INT DEFAULT 0,
    "REMARK" VARCHAR(255) DEFAULT '',
    "STATUS" INT DEFAULT 1 NOT NULL,
    "DELETED" INT DEFAULT 0 NOT NULL,
    "CREATE_BY" BIGINT,
    "CREATE_DATE" TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP(),
    "UPDATE_BY" BIGINT,
    "UPDATE_DATE" TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP(),
    NOT CLUSTER PRIMARY KEY("ID"),
    UNIQUE("CODE")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "SYS_ROUTE" IS '路由表';
COMMENT ON COLUMN "SYS_ROUTE"."CACHEABLE" IS '是否缓存保活';
COMMENT ON COLUMN "SYS_ROUTE"."CODE" IS '编码';
COMMENT ON COLUMN "SYS_ROUTE"."COMPATIBILITY" IS '是否降级兼容浏览器';
COMMENT ON COLUMN "SYS_ROUTE"."CREATE_BY" IS '创建人';
COMMENT ON COLUMN "SYS_ROUTE"."CREATE_DATE" IS '创建时间';
COMMENT ON COLUMN "SYS_ROUTE"."DELETED" IS '是否删除标记 - 0:正常,1:已删除';
COMMENT ON COLUMN "SYS_ROUTE"."HOST" IS '域名';
COMMENT ON COLUMN "SYS_ROUTE"."ICON" IS '图标';
COMMENT ON COLUMN "SYS_ROUTE"."ID" IS '主键';
COMMENT ON COLUMN "SYS_ROUTE"."NAME" IS '名称';
COMMENT ON COLUMN "SYS_ROUTE"."PARENT_MENU_ID" IS '父级菜单';
COMMENT ON COLUMN "SYS_ROUTE"."PATH" IS '地址';
COMMENT ON COLUMN "SYS_ROUTE"."REMARK" IS '备注';
COMMENT ON COLUMN "SYS_ROUTE"."SORT" IS '排序值';
COMMENT ON COLUMN "SYS_ROUTE"."STATUS" IS '状态 - 0:停用,1:启用';
COMMENT ON COLUMN "SYS_ROUTE"."UPDATE_BY" IS '最后更新人';
COMMENT ON COLUMN "SYS_ROUTE"."UPDATE_DATE" IS '最后更新时间';


CREATE UNIQUE  INDEX "INDEX3410298118100" ON "SYS_ROUTE"("ID" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

