export function componentRulesType(obj): string {
  // 额外type处理
  if (obj.componentProps?.multiple) return 'array';
  if ((!obj.componentProps?.multiple && obj.component === 'NSelect') || obj.component === 'NInputNumber')
    return 'number';
  return '';
}
