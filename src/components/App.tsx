import * as React from 'react';

export default class App extends React.Component<any, void> {
    render() {
        return (
            <div>
                <h1>RevCRM</h1>
                {this.props.children}
            </div>
        );
    }
}
