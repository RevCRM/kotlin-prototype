
import * as React from 'react';

interface IBasePageProps {
    title: string;
    scripts: string[];
    css: string[];
}

export function BasePage(props: IBasePageProps) {
    return (
        <html>
            <head>
                <title>{props.title}</title>
                {props.css.map((url, idx) => (
                    <link rel="stylesheet" href={url} key={idx} />
                ))}
            </head>
            <body style={{background: '#EEEEEE'}}>
                <div id="app">Loading...</div>
                {props.scripts.map((url, idx) => (
                    <script src={url} key={idx} />
                ))}
            </body>
        </html>
    );
}
