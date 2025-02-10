const demo: AuthRoute.Route = {
  name: 'demo',
  path: '/demo',
  component: 'basic',
  children: [
    {
      name: 'demo_list',
      path: '/demo/list',
      component: 'self',
      meta: {
        title: '列表页',
        icon: 'mdi:menu'
      }
    }
  ],
  meta: {
    title: '示例',
    icon: 'carbon:dashboard',
    order: 1
  }
};

export default demo;
