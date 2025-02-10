const system: AuthRoute.Route = {
  name: 'system',
  path: '/system',
  component: 'basic',
  children: [
    {
      name: 'system_dataMgmt',
      path: '/system/dataMgmt',
      component: 'self',
      meta: {
        title: '数据字典',
        icon: 'mdi:air-filter'
      }
    },
    {
      name: 'system_orgMgmt',
      path: '/system/orgMgmt',
      component: 'multi',
      children: [
        {
          name: 'system_orgMgmt_userMgmt',
          path: '/system/orgMgmt/userMgmt',
          component: 'self',
          meta: {
            title: '用户管理',
            icon: 'mdi:account'
          }
        },
        {
          name: 'system_orgMgmt_roleMgmt',
          path: '/system/orgMgmt/roleMgmt',
          component: 'self',
          meta: {
            title: '角色管理',
            icon: 'mdi:account-star'
          }
        },
        {
          name: 'system_orgMgmt_organizationMgmt',
          path: '/system/orgMgmt/organizationMgmt',
          component: 'self',
          meta: {
            title: '组织机构管理',
            icon: 'mdi:account-circle'
          }
        }
      ],
      meta: {
        title: '组织管理',
        icon: 'mdi:menu'
      }
    },
    {
      name: 'system_functionMgmt',
      path: '/system/functionMgmt',
      component: 'multi',
      children: [
        {
          name: 'system_functionMgmt_menuMgmt',
          path: '/system/functionMgmt/menuMgmt',
          component: 'self',
          meta: {
            title: '菜单管理',
            icon: 'mdi:menu'
          }
        },
        {
          name: 'system_functionMgmt_resourceMgmt',
          path: '/system/functionMgmt/resourceMgmt',
          component: 'self',
          meta: {
            title: '资源管理',
            icon: 'mdi:abugida-devanagari'
          }
        },
        {
          name: 'system_functionMgmt_operationMgmt',
          path: '/system/functionMgmt/operationMgmt',
          component: 'self',
          meta: {
            title: '操作管理',
            icon: 'mdi:account-circle'
          }
        },
        {
          name: 'system_functionMgmt_permissionMgmt',
          path: '/system/functionMgmt/permissionMgmt',
          component: 'self',
          meta: {
            title: '权限管理',
            icon: 'mdi:account-circle'
          }
        }
      ],
      meta: {
        title: '功能管理',
        icon: 'mdi:abacus'
      }
    }
  ],
  meta: {
    title: '系统管理',
    icon: 'carbon:settings',
    order: 1
  }
};

export default system;
