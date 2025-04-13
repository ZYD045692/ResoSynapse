import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
    state: () => ({
        userId: 17,
        isAuthenticated: false
    }),
    actions: {
        setUserId(userId: number) {
            this.userId = userId;
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