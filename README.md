# ddns server

aliyun

## 环境变量说明

| 环境变量                            | 说明                           | 示例值                               |
|---------------------------------|------------------------------|-----------------------------------|
| JAVA_OPTIONS                    | java启动参数                     | `-Xmx64M`                         |
| SERVER_PORT                     | springmvc端口                  | `8080`                            |
| ACCESS_TOKEN_KEY                | ddns-server的token header的key | `access-token`                    |
| ACCESS_TOKEN_LIST               | ddns-server的token header的值列表 | `xxx,yyy,zzz`                     |
| ENDPOINT                        | 请求API                        | `alidns.cn-shanghai.aliyuncs.com` |
| ALIBABA_CLOUD_ACCESS_KEY_ID     | 阿里云AccessKeyId               | 无                                 |
| ALIBABA_CLOUD_ACCESS_KEY_SECRET | 阿里云AccessKeySecret           | 无                                 |
| DDNS_DOMAIN_ACCESS_CONTROL_LIST | 阿里云解析的域名子集，用作控制              | `a.com,b.com `                    |
| DDNS_EXCLUDE_RR_LIST            | 不允许的域名解析列表                   | `ddns,api`                        |
| DDNS_TEST_SUBDOMAIN             | 测试用例使用                       | `a.b.com `                        |

## docker-compose部署

[docker-compose.yml](docker-compose.yml)

## nginx 反向代理参考

```nginx configuration
    location = /api/ddns/invoke {
        proxy_http_version 1.1;
        proxy_set_header Host $server_name:$server_port;
        proxy_set_header X-Forwarded-Host $http_host; # necessary for proper absolute redirects and TeamCity CSRF check
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header X-Forwarded-For $remote_addr;
        proxy_pass http://127.0.0.1:8080/ddns/invoke;
    }

    location = /api/ddns/invokeGetIpByServletRequest {
        proxy_http_version 1.1;
        proxy_set_header Host $server_name:$server_port;
        proxy_set_header X-Forwarded-Host $http_host; # necessary for proper absolute redirects and TeamCity CSRF check
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header X-Forwarded-For $remote_addr;
        proxy_pass http://127.0.0.1:8080/ddns/invokeGetIpByServletRequest;
    }
```

## dns_curl.sh 使用

```shell
./ddns_curl.sh "<dns_server_api>" "<access-token_key>" "<access-token_value>" "<domainName>" "<rr>"
```

- `dns_server_api`： dns-server 的 api
- `access-token_key` access-token 的 key
- `access-token_value` access-token 的 值
- `domainName` 域名
- `rr` 解析记录

**结合linux的定时任务(e.g. 每5分钟执行)**

```shell
*/5 * * * * bash /opt/ddns_curl/ddns_curl.sh "<dns_server_api>" "<access-token_key>" "<access-token_value>" "<domainName>" "<rr>" >>/opt/ddns_curl/ddns.log
```
