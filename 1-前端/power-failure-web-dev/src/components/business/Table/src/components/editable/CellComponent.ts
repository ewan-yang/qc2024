import type { FunctionalComponent, defineComponent } from 'vue';
import { h } from 'vue';
import { NPopover } from 'naive-ui';
import { componentMap } from '@/components/business/Table/src/componentMap';
import type { ComponentType } from '../../types/componentType';

export interface ComponentProps {
  component: ComponentType;
  rule: boolean;
  popoverVisible: boolean;
  ruleMessage: string;
}

export const CellComponent: FunctionalComponent = (
  { component = 'NInput', rule = true, ruleMessage, popoverVisible }: ComponentProps,
  { attrs }
) => {
  const Comp = componentMap.get(component) as typeof defineComponent;

  const DefaultComp = h(Comp, attrs);
  if (!rule) {
    return DefaultComp;
  }
  return h(
    NPopover,
    { 'display-directive': 'show', show: Boolean(popoverVisible), manual: 'manual' },
    {
      trigger: () => DefaultComp,
      default: () =>
        h(
          'span',
          {
            style: {
              color: 'red',
              width: '90px',
              display: 'inline-block'
            }
          },
          {
            default: () => ruleMessage
          }
        )
    }
  );
};
