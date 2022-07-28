
export class DeviceModuleOptions {
    constructor(
        public modalHeader: string = 'MyApp',
        public dialogsMode: 'native' | 'ionic' = 'native',
        public loadingMode: 'native' | 'ionic' = 'native',
    ) { }
}
