import { propTypes } from '@/utils';

export const basicProps = {
  title: {
    type: String,
    default: ''
  },
  // 确认按钮文字
  subBtuText: {
    type: String,
    default: '确认'
  },
  showSubmit: propTypes.bool.def(true),
  modalType: {
    type: String,
    default: 'm'
  }
};
