
import * as React from 'react';

interface IBasePageProps {
    title: string;
    scripts: string[];
}

export default function BasePage(props: IBasePageProps) {
    return (
        <html>
            <head>
                <title>{props.title}</title>
            </head>
            <body>
                <div id="app">Loading...</div>
                {props.scripts.map((url, idx) => (
                    <script src={url} key={idx} />
                ))}
            </body>
        </html>
    );
}
