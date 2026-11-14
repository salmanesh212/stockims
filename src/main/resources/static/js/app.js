(() => {
  const themeToggle = document.querySelector('[data-theme-toggle]');
  const prefersLight = window.matchMedia?.('(prefers-color-scheme: light)').matches;
  const savedTheme = localStorage.getItem('stockims-theme');
  if (savedTheme === 'light' || (!savedTheme && prefersLight)) document.body.classList.add('light');
  const updateThemeLabel = () => {
    if (!themeToggle) return;
    themeToggle.setAttribute('aria-label', document.body.classList.contains('light') ? 'Activer le thème sombre' : 'Activer le thème clair');
  };
  themeToggle?.addEventListener('click', () => {
    const light = document.body.classList.toggle('light');
    localStorage.setItem('stockims-theme', light ? 'light' : 'dark');
    updateThemeLabel();
  });
  updateThemeLabel();

  const search = document.querySelector('#article-search');
  if (search) {
    const rows = [...document.querySelectorAll('#article-rows .article-row')];
    const empty = document.querySelector('#search-empty');
    const normalize = (value) => value.normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLocaleLowerCase('fr').trim();
    search.addEventListener('input', () => {
      const query = normalize(search.value);
      let visible = 0;
      rows.forEach((row) => {
        const matches = normalize(row.textContent).includes(query);
        row.hidden = !matches;
        if (matches) visible += 1;
      });
      if (empty) empty.hidden = visible !== 0;
    });
    document.addEventListener('keydown', (event) => {
      if (event.key === '/' && document.activeElement !== search && !['INPUT', 'TEXTAREA'].includes(document.activeElement?.tagName)) {
        event.preventDefault();
        search.focus();
      }
    });
  }

  const toggle = document.querySelector('[data-menu-toggle]');
  const close = () => {
    document.body.classList.remove('menu-open');
    toggle?.setAttribute('aria-expanded', 'false');
  };
  toggle?.addEventListener('click', () => {
    const open = document.body.classList.toggle('menu-open');
    toggle.setAttribute('aria-expanded', String(open));
  });
  document.querySelectorAll('[data-menu-close]').forEach((item) => item.addEventListener('click', close));
  document.addEventListener('keydown', (event) => { if (event.key === 'Escape') close(); });

  document.querySelectorAll('.alert-success').forEach((alert) => {
    window.setTimeout(() => alert.classList.add('alert-dismissed'), 4000);
  });
  document.querySelectorAll('.stock-value[data-stock-value]').forEach((element) => {
    const target = Number(element.dataset.stockValue);
    if (window.matchMedia?.('(prefers-reduced-motion: reduce)').matches || !Number.isFinite(target)) return;
    const start = performance.now();
    const tick = (now) => {
      const progress = Math.min((now - start) / 500, 1);
      element.textContent = String(Math.round(target * progress));
      if (progress < 1) window.requestAnimationFrame(tick);
      else element.textContent = String(target);
    };
    window.requestAnimationFrame(tick);
  });

  const askConfirmation = (message) => {
    const dialog = document.querySelector('#sortie-dialog');
    if (!dialog || typeof dialog.showModal !== 'function') return window.confirm(message);
    dialog.querySelector('[data-dialog-message]').textContent = message;
    return new Promise((resolve) => {
      dialog.addEventListener('close', () => resolve(dialog.returnValue === 'confirm'), { once: true });
      dialog.showModal();
    });
  };
  document.querySelectorAll('[data-confirm-sortie]').forEach((form) => {
    let submitted = false;
    form.addEventListener('submit', async (event) => {
      if (submitted) { event.preventDefault(); return; }
      event.preventDefault();
      const quantity = form.querySelector('input[type="number"]')?.value;
      const article = form.dataset.article || 'cet article';
      if (!await askConfirmation(`Confirmer la sortie de ${quantity || 'cette quantité'} unité(s) pour « ${article} » ?`)) return;
      submitted = true;
      const button = form.querySelector('button[type="submit"]');
      if (button) { button.disabled = true; button.setAttribute('aria-busy', 'true'); button.textContent = 'Enregistrement…'; }
      HTMLFormElement.prototype.submit.call(form);
    });
  });
})();
