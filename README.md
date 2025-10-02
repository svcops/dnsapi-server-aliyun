# dnsapi server aliyun

<!-- TOC -->
* [dnsapi server aliyun](#dnsapi-server-aliyun)
  * [环境变量说明](#环境变量说明)
  * [`docker-compose`部署](#docker-compose部署)
  * [`nginx`反向代理](#nginx反向代理)
  * [DDNS HTTP 请求说明](#ddns-http-请求说明)
    * [`/ddns/invoke`](#ddnsinvoke)
    * [`/ddns/invokeGetIpAutomatic`](#ddnsinvokegetipautomatic)
  * [`ddns_curl.sh`使用](#ddns_curlsh使用)
<!-- TOC -->

## 环境变量说明

| 环境变量                               | 说明                           | 示例值                                                         |
|------------------------------------|------------------------------|-------------------------------------------------------------|
| JAVA_OPTIONS                       | java启动参数                     | `-Xmx64M`                                                   |
| SERVER_PORT                        | springmvc端口                  | `8080`                                                      |
| ACCESS_TOKEN_KEY                   | ddns-server的token header的key | `access-token`                                              |
| ACCESS_TOKEN_LIST                  | ddns-server的token header的值列表 | `xxx,yyy,zzz`                                               |
| ENDPOINT                           | 请求API                        | 默认 `alidns.cn-shanghai.aliyuncs.com`                        |
| ALIBABA_CLOUD_ACCESS_KEY_LIST      | 阿里云AccessKey JsonArray       | `[{"id":"aaa","secret":"bbb"},{"id":"xxx","secret":"yyy"}]` |
| DNS_API_DOMAIN_ACCESS_CONTROL_LIST | 阿里云解析的域名子集，用作控制              | `a.com,b.com `                                              |
| DNS_API_EXCLUDE_RR_LIST            | 不允许的域名解析列表                   | `ddns,api`                                                  |

## `docker-compose`部署

[docker-compose.yml 文件](docker-compose.yml)

## `nginx`反向代理

e.g.

```nginx configuration
    location = /api/ddns/invoke {
        proxy_http_version 1.1;
        proxy_set_header Host $server_name:$server_port;
        proxy_set_header X-Forwarded-Host $http_host; # necessary for proper absolute redirects and TeamCity CSRF check
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header X-Forwarded-For $remote_addr;
        proxy_pass http://127.0.0.1:8080/ddns/invoke;
    }
    
    location = /api/ddns/invokeGetIpAutomatic {
        proxy_http_version 1.1;
        proxy_set_header Host $server_name:$server_port;
        proxy_set_header X-Forwarded-Host $http_host; # necessary for proper absolute redirects and TeamCity CSRF check
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header X-Forwarded-For $remote_addr;
        proxy_pass http://127.0.0.1:8080/ddns/invokeGetIpAutomatic;
    }

```

## DDNS HTTP 请求说明

### `/ddns/invoke`

```http request
POST ROOT_URI/ddns/invoke
Content-Type: application/json
access-token: <token>
Accept: application/json

{
 "domainName": "example.com",
 "rr": "ddns",
 "ipv4": "127.0.0.1"
}
```

### `/ddns/invokeGetIpAutomatic`

[DDnsController.java](src/main/java/io/intellij/devops/ddns/server/controller/DDnsController.java)

> 根据 HttpServletRequest 获取请求的 ip 地址

```http request
POST ROOT_URI/ddns/invokeGetIpAutomatic
Content-Type: application/json
access-token: <token>
Accept: application/json

{
 "domainName": "example.com",
 "rr": "ddns"
}
```

## `ddns_curl.sh`使用

> **notice**: use endpoint `/ddns/invokeGetIpAutomatic`

[ddns_curl.sh 文件](ddns_curl.sh)

```shell
./ddns_curl.sh "<dns_server_root_uri>" "<access-token_key>" "<access-token_value>" "<domainName>" "<rr>"
```

- `dns_server_root_uri`： dns-server 的 root uri
  - e.g. `http://localhost:8080/api`
- `access-token_key` access-token 的 key
  - e.g. `access-token`
- `access-token_value` access-token 的 值
  - e.g. `xxx`
- `domainName` 域名
  - e.g. `example.com`
- `rr` 解析记录
  - e.g. `ddns`

**结合linux的定时任务(e.g. 每5分钟执行)**

```shell
*/5 * * * * bash /opt/ddns_curl/ddns_curl.sh "<dns_server_root_uri>" "<access-token_key>" "<access-token_value>" "<domainName>" "<rr>" >>/opt/ddns_curl/ddns.log
```
