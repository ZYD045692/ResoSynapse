import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
    state: () => ({
        userId: 0,
        userName: '',
        isAuthenticated: false
    }),
    actions: {
        setUserId(userId: number) {
            this.userId = userId;
            localStorage.setItem('userId', userId.toString());
        },
        setUserName(userName: string) {
            this.userName = userName;
            localStorage.setItem('userName', userName);
        },
        setAuthenticated(status: boolean) {
            this.isAuthenticated = status;
            localStorage.setItem('isAuthenticated', status.toString());
        },
        checkAuth() {
            const status = localStorage.getItem('isAuthenticated') === 'true';
            this.isAuthenticated = status;
            return status;
        }
    },
});