import type { Ref } from 'vue';
import { unref } from 'vue';
import type { FormItemRule } from 'naive-ui';
import { REGEXP_CODE_ONE, REGEXP_EMAIL, REGEXP_PHONE, REGEXP_PWD } from '@/config';

/** 创建自定义错误信息的必填表单规则 */
export const createRequiredFormRule = (message = '不能为空'): FormItemRule => ({ required: true, message });

export const requiredFormRule = createRequiredFormRule();

/** 表单规则 */
interface CustomFormRules {
  /** 手机号码 */
  phone: FormItemRule[];
  /** 密码 */
  pwd: FormItemRule[];
  /** 验证码 */
  code: FormItemRule[];
  /** 邮箱 */
  email: FormItemRule[];
  passWo: FormItemRule[];
  oldPassWo: FormItemRule[];
}

/** 表单规则 */
export const formRules: CustomFormRules = {
  phone: [
    createRequiredFormRule("请输入手机号码"),
    { pattern: REGEXP_PHONE, message: "手机号码格式错误", trigger: "input" },
  ],
  pwd: [
    createRequiredFormRule("请输入密码"),
    {
      pattern: REGEXP_PWD,
      message: "密码为6-18位数字/字符/符号，至少2种组合",
      trigger: "input",
    },
  ],
  oldPassWo: [
    createRequiredFormRule("请输入原密码"),
  ],
  passWo: [
    createRequiredFormRule("请输入密码"),
    {
      pattern:
        /^(?=.*[a-z])(?=.*\d)(?=.*[A-Z])[a-zA-Z\d!@#$%^&*()\_+=-{}[\];:'",.<>/?]{8,}$/,
      message: "密码必须包含大小写字母和数字的组合，可以使用特殊符号，至少8位",
      trigger: "input",
    },
  ],
  code: [
    createRequiredFormRule("请输入验证码"),
    { pattern: REGEXP_CODE_ONE, message: "验证码格式错误", trigger: "input" },
  ],
  email: [{ pattern: REGEXP_EMAIL, message: "邮箱格式错误", trigger: "blur" }],
};

/** 是否为空字符串 */
function isBlankString(str: string) {
  return str.trim() === '';
}

/** 获取确认密码的表单规则 */
export function getConfirmPwdRule(pwd: Ref<string>) {
  const confirmPwdRule: FormItemRule[] = [
    { required: true, message: '请输入确认密码' },
    {
      validator: (rule, value) => {
        if (!isBlankString(value) && value !== pwd.value) {
          return Promise.reject(rule.message);
        }
        return Promise.resolve();
      },
      message: '输入的值与密码不一致',
      trigger: 'input'
    }
  ];
  return confirmPwdRule;
}

/** 获取图片验证码的表单规则 */
export function getImgCodeRule(imgCode: Ref<string>) {
  const imgCodeRule: FormItemRule[] = [
    { required: true, message: '请输入验证码' },
    {
      validator: (rule, value) => {
        if (!isBlankString(value) && value !== imgCode.value) {
          return Promise.reject(rule.message);
        }
        return Promise.resolve();
      },
      message: '验证码不正确',
      trigger: 'blur'
    }
  ];
  return imgCodeRule;
}

// dynamic use hook props
export function getDynamicProps<T extends {}, U>(props: T): Partial<U> {
  const ret: Recordable = {};

  Object.keys(props).map(key => {
    ret[key] = unref((props as Recordable)[key]);
  });

  return ret as Partial<U>;
}
