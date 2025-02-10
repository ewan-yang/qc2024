/** 请求服务的环境配置 */
type ServiceEnv = Record<ServiceEnvType, ServiceEnvConfig>;

/** 不同请求服务的环境配置 */
const serviceEnv: ServiceEnv = {
  dev: {
    // url: 'http://172.16.0.25:19000',
    // urlPattern: '/tecpie-platform-user/api/v1',
    // url: "http://172.16.0.90:19000",
    url: "http://172.16.0.90:8080",
    urlPattern: "/relay-task-service/api/v1",
    secondUrl: "http://localhost:8081",
    secondUrlPattern: "/second-url-pattern",
  },
  test: {
    // url: 'http://172.16.0.25:19000',
    // urlPattern: '/tecpie-platform-user/api/v1',
    url: "http://172.16.0.90:18090",
    urlPattern: "/relay-task-service/api/v1",
    secondUrl: "http://localhost:18091",
    secondUrlPattern: "/second-url-pattern",
  },
  uat: {
    // http://218.92.246.37:18080公网
    // http://10.131.141.201:31099内网
    url: "",
    urlPattern: "",
    secondUrl: "http://localhost:8081",
    secondUrlPattern: "/second-url-pattern",
  },
  prod: {
    url: "",
    urlPattern: "",
    secondUrl: "http://localhost:8081",
    secondUrlPattern: "/second-url-pattern",
  },
};

/**
 * 获取当前环境模式下的请求服务的配置
 * @param env 环境
 */
export function getServiceEnvConfig(env: ImportMetaEnv) {
  const { VITE_SERVICE_ENV = 'dev' } = env;

  const config = serviceEnv[VITE_SERVICE_ENV];

  return config;
}
