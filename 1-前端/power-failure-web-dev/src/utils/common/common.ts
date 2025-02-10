export function convertOptions({
  sourceData = [],
  targetKeys = ['label', 'value', 'code'],
  sourceKeys = ['name', 'id', 'code']
}) {
  let obj;
  return sourceData?.map(_item => {
    obj = {};
    targetKeys.forEach((tK, idx) => {
      obj[tK] = _item[sourceKeys[idx]];
    });
    return obj;
  });
}
