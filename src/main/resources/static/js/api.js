/**
 * LUNEA Client API & UI Helper Library
 */
const LUNEA = (function () {
    'use strict';

    // Format number to Vietnamese Dong currency
    function formatVND(amount) {
        if (amount === null || amount === undefined || isNaN(amount)) return '0 ₫';
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND',
            maximumFractionDigits: 0
        }).format(amount).replace('VND', '₫');
    }

    // Floating Luxury Toast Notification
    function showToast(message, type = 'info', duration = 3500) {
        let container = document.getElementById('toast-container');
        if (!container) {
            container = document.createElement('div');
            container.id = 'toast-container';
            document.body.appendChild(container);
        }

        const toast = document.createElement('div');
        toast.className = `lunea-toast ${type}`;

        let iconSvg = '';
        if (type === 'success') {
            iconSvg = '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="20 6 9 17 4 12"/></svg>';
        } else if (type === 'error') {
            iconSvg = '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg>';
        } else {
            iconSvg = '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></svg>';
        }

        toast.innerHTML = `
            <div class="lunea-toast-icon">${iconSvg}</div>
            <div class="lunea-toast-message">${message}</div>
        `;

        container.appendChild(toast);

        // Animate in
        requestAnimationFrame(() => {
            toast.classList.add('show');
        });

        // Auto remove
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => {
                if (toast.parentElement) toast.parentElement.removeChild(toast);
            }, 300);
        }, duration);
    }

    // Unified API Fetch Wrapper
    async function apiFetch(url, options = {}) {
        const defaultHeaders = {
            'X-Requested-With': 'fetch',
            'Content-Type': 'application/json'
        };

        const config = {
            ...options,
            headers: {
                ...defaultHeaders,
                ...(options.headers || {})
            }
        };

        try {
            const response = await fetch(url, config);

            // 401 Unauthorized
            if (response.status === 401) {
                showToast('Vui lòng đăng nhập để tiếp tục thao tác.', 'error', 3000);
                setTimeout(() => {
                    const currentPath = window.location.pathname + window.location.search;
                    window.location.href = `/login?redirect=${encodeURIComponent(currentPath)}`;
                }, 1200);
                throw new Error('UNAUTHENTICATED');
            }

            // 403 Forbidden
            if (response.status === 403) {
                showToast('Bạn không có quyền thực hiện thao tác này.', 'error', 3500);
                throw new Error('ACCESS_DENIED');
            }

            // 204 No Content
            if (response.status === 204) {
                return null;
            }

            // Parse response body
            const contentType = response.headers.get('content-type');
            const isJson = contentType && contentType.includes('application/json');
            const data = isJson ? await response.json() : await response.text();

            if (!response.ok) {
                const errorMessage = (data && data.message) ? data.message : 'Đã có lỗi xảy ra. Vui lòng thử lại!';
                showToast(errorMessage, 'error', 4000);
                const error = new Error(errorMessage);
                error.data = data;
                error.status = response.status;
                throw error;
            }

            return data;
        } catch (err) {
            if (err.message !== 'UNAUTHENTICATED' && err.message !== 'ACCESS_DENIED' && !err.status) {
                showToast(err.message || 'Lỗi kết nối máy chủ', 'error', 3500);
            }
            throw err;
        }
    }

    // Cart Helper Methods
    async function updateCartBadge() {
        try {
            const cart = await apiFetch('/api/cart', { method: 'GET' });
            const badges = document.querySelectorAll('.cart-badge-count');
            badges.forEach(b => {
                b.textContent = cart && cart.totalItems ? cart.totalItems : 0;
            });
            return cart;
        } catch (e) {
            // Unauthenticated or empty
            return null;
        }
    }

    async function addToCart(skuId, quantity = 1) {
        try {
            const res = await apiFetch('/api/cart/items', {
                method: 'POST',
                body: JSON.stringify({ skuId, quantity })
            });
            showToast('Đã thêm sản phẩm vào giỏ hàng thành công!', 'success');
            await updateCartBadge();
            return res;
        } catch (e) {
            // Error handled by apiFetch
            throw e;
        }
    }

    // Logout Helper
    async function logout() {
        try {
            await apiFetch('/api/auth/logout', { method: 'POST' });
            showToast('Đăng xuất thành công!', 'success');
            setTimeout(() => {
                window.location.href = '/';
            }, 500);
        } catch (e) {
            window.location.href = '/login';
        }
    }

    // Initialize on page load
    document.addEventListener('DOMContentLoaded', () => {
        // If user is logged in (cart badge exists in DOM), refresh cart count
        const badge = document.querySelector('.cart-badge-count');
        if (badge) {
            updateCartBadge();
        }
    });

    return {
        formatVND,
        showToast,
        toastSuccess: (msg) => showToast(msg, 'success'),
        toastError: (msg) => showToast(msg, 'error'),
        apiFetch,
        addToCart,
        updateCartBadge,
        logout
    };
})();
