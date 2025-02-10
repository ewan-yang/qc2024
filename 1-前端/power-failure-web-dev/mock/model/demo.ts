import { Random } from 'mockjs';

function doCustomTimes(times: number, callback: any) {
  let i = -1;
  // eslint-disable-next-line no-plusplus
  while (++i < times) {
    callback(i);
  }
}

export const tableList = pageSize => {
  const result: any[] = [];
  doCustomTimes(pageSize, () => {
    result.push({
      id: '@integer(10,999999)',
      beginTime: '@datetime',
      endTime: '@datetime',
      address: '@city()',
      name: '@cname()',
      avatar: Random.image('400x400', Random.color(), Random.color(), Random.first()),
      date: `@date('yyyy-MM-dd')`,
      time: `@time('HH:mm')`,
      'no|100000-10000000': 100000,
      'status|1': [true, false],
      code: '@integer(10,99999)',
      remark: '@string(3,8)'
    });
  });
  return result;
};
