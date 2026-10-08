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
        const skipAuthRedirect = options.skipAuthRedirect || false;
        const skipAuthToast = options.skipAuthToast || false;
        const isAuthPage = window.location.pathname.startsWith('/login') || window.location.pathname.startsWith('/register');

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
                if (!skipAuthToast && !isAuthPage) {
                    showToast('Vui lòng đăng nhập để tiếp tục thao tác.', 'error', 3000);
                }
                if (!skipAuthRedirect && !isAuthPage) {
                    setTimeout(() => {
                        const currentPath = window.location.pathname + window.location.search;
                        window.location.href = `/login?redirect=${encodeURIComponent(currentPath)}`;
                    }, 1200);
                }
                throw new Error('UNAUTHENTICATED');
            }

            // 403 Forbidden
            if (response.status === 403) {
                if (!isAuthPage) {
                    showToast('Bạn không có quyền thực hiện thao tác này.', 'error', 3500);
                }
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
            if (err.message !== 'UNAUTHENTICATED' && err.message !== 'ACCESS_DENIED' && !err.status && !skipAuthToast) {
                showToast(err.message || 'Lỗi kết nối máy chủ', 'error', 3500);
            }
            throw err;
        }
    }

    // Cart Helper Methods
    async function updateCartBadge() {
        const isAuthPage = window.location.pathname.startsWith('/login') || window.location.pathname.startsWith('/register');
        if (isAuthPage) return null;

        try {
            const cart = await apiFetch('/api/cart', { 
                method: 'GET',
                skipAuthRedirect: true,
                skipAuthToast: true
            });
            const badges = document.querySelectorAll('.cart-badge-count');
            badges.forEach(b => {
                b.textContent = cart && cart.totalItems ? cart.totalItems : 0;
            });
            return cart;
        } catch (e) {
            // Unauthenticated guest or empty cart
            const badges = document.querySelectorAll('.cart-badge-count');
            badges.forEach(b => {
                b.textContent = 0;
            });
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

    // Interactive Wishlist Toggle
    function toggleWishlist(btn) {
        if (!btn) return;
        const icon = btn.querySelector('i');
        const isActive = btn.classList.toggle('active');
        if (isActive) {
            if (icon) {
                icon.className = 'bi bi-heart-fill';
                icon.style.color = '#f43f5e';
            }
            showToast('Đã thêm sản phẩm vào danh sách yêu thích của bạn.', 'success');
        } else {
            if (icon) {
                icon.className = 'bi bi-heart';
                icon.style.color = '';
            }
            showToast('Đã bỏ sản phẩm khỏi danh sách yêu thích.', 'info');
        }
    }

    // Scroll Motion & Cinematic Reveal System (European Agency UX)
    function initScrollMotion() {
        // 1. Header scroll blur & compact mode
        const header = document.querySelector('.lunea-header');
        if (header) {
            const onScroll = () => {
                if (window.scrollY > 30) {
                    header.classList.add('header-scrolled');
                } else {
                    header.classList.remove('header-scrolled');
                }
            };
            window.addEventListener('scroll', onScroll, { passive: true });
            onScroll();
        }

        // 2. IntersectionObserver for smooth scroll reveals
        const revealElements = document.querySelectorAll('.reveal-on-scroll, .stagger-cards');
        if ('IntersectionObserver' in window && revealElements.length > 0) {
            const revealObserver = new IntersectionObserver((entries, observer) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        entry.target.classList.add('is-revealed');
                        observer.unobserve(entry.target);
                    }
                });
            }, {
                root: null,
                threshold: 0.12,
                rootMargin: '0px 0px -40px 0px'
            });

            revealElements.forEach(el => revealObserver.observe(el));
        } else {
            // Fallback for older browsers
            revealElements.forEach(el => el.classList.add('is-revealed'));
        }

        // 3. Subtle Hero Moon Cursor Parallax
        const heroVisual = document.querySelector('.hero-visual');
        const moonWrap = document.querySelector('.moon-image-wrapper');
        if (heroVisual && moonWrap) {
            heroVisual.addEventListener('mousemove', (e) => {
                const rect = heroVisual.getBoundingClientRect();
                const x = (e.clientX - rect.left) / rect.width - 0.5;
                const y = (e.clientY - rect.top) / rect.height - 0.5;
                moonWrap.style.transform = `translate(${x * 16}px, ${y * 16}px) rotate(${x * 4}deg)`;
            });

            heroVisual.addEventListener('mouseleave', () => {
                moonWrap.style.transform = '';
            });
        }
    }

    // Initialize on page load
    document.addEventListener('DOMContentLoaded', () => {
        // Refresh cart count (skip for auth pages)
        const isAuthPage = window.location.pathname.startsWith('/login') || window.location.pathname.startsWith('/register');
        const badge = document.querySelector('.cart-badge-count');
        if (badge && !isAuthPage) {
            updateCartBadge();
        }

        // Initialize scroll motion
        initScrollMotion();
    });

    async function logout(e) {
        if (e && e.preventDefault) {
            e.preventDefault();
        }
        try {
            await fetch('/api/auth/logout', { 
                method: 'POST',
                credentials: 'include',
                headers: { 
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'fetch'
                }
            });
        } catch (e) {
            console.error('Logout error:', e);
        }
        // Force full navigation to /logout to guarantee cookie eviction and redirect to /login
        window.location.href = '/logout';
    }

    return {
        formatVND,
        showToast,
        toastSuccess: (msg) => showToast(msg, 'success'),
        toastError: (msg) => showToast(msg, 'error'),
        apiFetch,
        addToCart,
        updateCartBadge,
        logout,
        toggleWishlist,
        initScrollMotion
    };
})();
