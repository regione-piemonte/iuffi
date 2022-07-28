module.exports = {
    env: {
        browser: true,
        node: true,
        es6: true,
    },
    parser: '@typescript-eslint/parser',
    parserOptions: {
        project: './tsconfig.json',
        ecmaFeatures: {
            jsx: true,
        },
    },
    extends: [
        'plugin:@typescript-eslint/recommended',
        'prettier/@typescript-eslint'
    ],
    rules: {
        'arrow-spacing': 'error',
        'arrow-parens': ['error', 'as-needed'],
        'comma-dangle': ['error', 'only-multiline'],
        'block-spacing': ['error', 'always'],
        'func-call-spacing': ['error', 'never'],
        'key-spacing': ['error', {
            beforeColon: false,
            afterColon: true,
            mode: 'strict'
        }],
        'keyword-spacing': ['error', {
            before: true,
            after: true
        }],
        indent: ['error', 4],
        'no-const-assign': 'error',
        'no-multiple-empty-lines': ['error', { max: 1, maxEOF: 0 }],
        'no-trailing-spaces': ['error'],
        'no-var': 'error',
        quotes: ['error', 'single'],
        'quote-props': ['error', 'as-needed'],
        'space-before-blocks': 'error',
        'space-before-function-paren': ['error', 'never'],
        camelcase: 2,
        'sort-imports': 'off',
        'no-extra-parens': 'off',
        "@typescript-eslint/explicit-module-boundary-types": ['error', {
            allowArgumentsExplicitlyTypedAsAny: true
        }],
        '@typescript-eslint/member-delimiter-style': 'error',
        '@typescript-eslint/no-explicit-any': 0,
        '@typescript-eslint/no-empty-function': 0,
        '@typescript-eslint/no-var-requires': 2,
        '@typescript-eslint/explicit-function-return-type': ['error', {
            allowExpressions: true,
            allowTypedFunctionExpressions: false,
        }],
        '@typescript-eslint/explicit-member-accessibility': ['error', {
            accessibility: 'explicit',
            ignoredMethodNames: [
                'ionViewDidLoad',
                'ionViewWillEnter',
                'ionViewDidEnter',
                'ionViewWillLeave',
                'ionViewDidLeave',
                'ionViewWillUnload',
                'ionViewCanEnter',
                'ionViewCanLeave',
                'ngOnChanges',
                'ngOnInit',
                'ngDoCheck',
                'ngOnDestroy',
                'ngAfterContentInit',
                'ngAfterContentChecked',
                'ngAfterViewInit',
                'ngAfterViewChecked'
            ],
            overrides: {
                constructors: 'off'
            }
        }],
        '@typescript-eslint/no-this-alias': [
            'error',
            {
                allowedNames: ['self'],
            },
        ],
        '@typescript-eslint/no-parameter-properties': ['error', {
            allows: [
                'public',
                'private',
                'protected'
            ]
        }]
    }
};
