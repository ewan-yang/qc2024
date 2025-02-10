import { dataTypeLabels } from '../../constants/index';

export function isNumber<T extends number>(data: T | unknown): data is T {
  return Object.prototype.toString.call(data) === dataTypeLabels.number;
}

export function isString<T extends string>(data: T | unknown): data is T {
  return Object.prototype.toString.call(data) === dataTypeLabels.string;
}

export function isBoolean<T extends boolean>(data: T | unknown): data is T {
  return Object.prototype.toString.call(data) === dataTypeLabels.boolean;
}

export function isNull<T extends null>(data: T | unknown): data is T {
  return Object.prototype.toString.call(data) === dataTypeLabels.null;
}

export function isUndefined<T extends undefined>(data: T | unknown): data is T {
  return Object.prototype.toString.call(data) === dataTypeLabels.undefined;
}

export function isObject<T extends Record<string, any>>(data: T | unknown): data is T {
  return Object.prototype.toString.call(data) === dataTypeLabels.object;
}

export function isArray<T extends any[]>(data: T | unknown): data is T {
  return Object.prototype.toString.call(data) === dataTypeLabels.array;
}

export function isFunction<T extends (...args: any[]) => any | void | never>(data: T | unknown): data is T {
  return Object.prototype.toString.call(data) === dataTypeLabels.function;
}

export function isDate<T extends Date>(data: T | unknown): data is T {
  return Object.prototype.toString.call(data) === dataTypeLabels.date;
}

export function isPromise<T extends Promise<any>>(data: T | unknown): data is T {
  return Object.prototype.toString.call(data) === dataTypeLabels.promise;
}

export function isSet<T extends Set<any>>(data: T | unknown): data is T {
  return Object.prototype.toString.call(data) === dataTypeLabels.set;
}

export function isMap<T extends Map<any, any>>(data: T | unknown): data is T {
  return Object.prototype.toString.call(data) === dataTypeLabels.map;
}

export function isFile<T extends File>(data: T | unknown): data is T {
  return Object.prototype.toString.call(data) === dataTypeLabels.file;
}
export function isNullOrUnDef(val: unknown): val is null | undefined {
  // eslint-disable-next-line @typescript-eslint/no-use-before-define
  return isUnDef(val) || isNull(val);
}

/**
 * @description: 是否已定义
 */
export const isDef = <T = unknown>(val?: T): val is T => {
  return typeof val !== 'undefined';
};

export const isUnDef = <T = unknown>(val?: T): val is T => {
  return !isDef(val);
};
export const isServer = typeof window === 'undefined';
