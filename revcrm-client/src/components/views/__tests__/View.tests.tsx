
import * as React from 'react';
import * as TestRenderer from 'react-test-renderer';
import { ViewManager } from '../ViewManager';
import { View } from '../View';
import { createMemoryHistory } from 'history';
import { UI, IPerspective, IView } from '../../../UIManager';
import { Typography } from '@material-ui/core';

describe('<View />', () => {
    let renderer: TestRenderer.ReactTestRenderer;
    let instance: TestRenderer.ReactTestInstance;

    const mockPerspective: IPerspective = {
        id: 'dashboard',
        title: 'Dashboard',
        views: {
            my: {
                title: 'My Dashboard',
                viewId: 'dashboard',
            },
        }
    };
    UI.registerPerspective(mockPerspective);

    const mockMyView: IView = {
        id: 'dashboard',
        model: null,
        component: () => <div id="mockMyDashboard" />
    };
    UI.registerView(mockMyView);

    describe('when a Perspective and View is matched', () => {

        beforeAll(() => {
            const history = createMemoryHistory({
                initialEntries: ['/dashboard/my']
            });
            renderer = TestRenderer.create(
                <ViewManager history={history}>
                    <View />
                </ViewManager>
            );
            instance = renderer.root;
        });

        it('renders the expected component', () => {
            expect(instance.findAll((el) =>
                el.type == 'div' && el.props.id == 'mockMyDashboard'
            ).length).toEqual(1);
        });

    });

    describe('when a Perspective and View is not matched', () => {

        beforeAll(() => {
            const history = createMemoryHistory({
                initialEntries: ['/no_existy']
            });
            renderer = TestRenderer.create(
                <ViewManager history={history}>
                    <View />
                </ViewManager>
            );
            instance = renderer.root;
        });

        it('renders the expected component', () => {
            expect(instance.findByType(Typography).props.children).toEqual('Not Found');
        });

    });

});
