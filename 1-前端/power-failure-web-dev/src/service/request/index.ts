import { getServiceEnvConfig } from '~/.env-config';
import { createRequest } from './request';

const { url, urlPattern, secondUrl, secondUrlPattern } = getServiceEnvConfig(import.meta.env);

const isHttpProxy = import.meta.env.VITE_HTTP_PROXY === 'Y';
console.log(isHttpProxy,'isHttpProxy')
export const request = createRequest({ baseURL: isHttpProxy ? url : url });

export const secondRequest = createRequest({ baseURL: isHttpProxy ? secondUrlPattern : secondUrl + secondUrlPattern });

export const mockRequest = createRequest({ baseURL: '/mock' });

export const mockAPIRequest = createRequest({ baseURL: '/mock/api/v1' });
