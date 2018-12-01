
export interface IPerspective {
    name: string;
    title: string;
    views: {
        [viewName: string]: string;
    };
}

export interface IView {
    name: string;
    model: string;
    related?: string[];
    component: React.ReactNode;
}
