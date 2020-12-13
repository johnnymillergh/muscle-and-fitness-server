// noinspection JSUnusedGlobalSymbols
const vm = new Vue({
    el: '#app',
    data: {
        projectArtifactId: null,
        version: null,
        applicationName: null,
        newPerson: {
            name: '',
            age: 0,
            sex: 'Male'
        },
        people: [{
            name: 'Jack',
            age: 30,
            sex: 'Male'
        }, {
            name: 'Bill',
            age: 26,
            sex: 'Male'
        }, {
            name: 'Tracy',
            age: 22,
            sex: 'Female'
        }, {
            name: 'Chris',
            age: 36,
            sex: 'Male'
        }]
    },
    methods: {
        /**
         * Create a person
         */
        createPerson: function () {
            this.people.push(this.newPerson);
            this.newPerson = {name: '', age: 0, sex: 'Male'}

        },
        /**
         * delete a person
         * @param index
         */
        deletePerson: function (index) {
            this.people.splice(index, 1);
        },
        handleClickSwaggerApiDocumentation: function () {
            const currentUrl = window.location.href;
            const split = currentUrl.split('/');
            window.location.href = `${split[0]}//${split[2]}/doc`;
        },
        handleClickVideoDemo: function () {
            const currentUrl = window.location.href;
            const split = currentUrl.split('/');
            window.location.href = `${split[0]}//${split[2]}/${split[3]}/video.html`;
        },
        getAppInfo: async function () {
            const currentUrl = window.location.href;
            const split = currentUrl.split('/');
            const baseUrl = `${split[0]}//${split[2]}`;
            const response = await fetch(`${baseUrl}/common/app-info`);
            const responseJson = await response.json();
            this.projectArtifactId = responseJson.data.projectArtifactId;
            this.version = responseJson.data.version;
            this.applicationName = this.projectArtifactId.toUpperCase();
            window.document.title = this.applicationName;
        }
    },
    mounted: async function () {
        this.getAppInfo();
    }
});
