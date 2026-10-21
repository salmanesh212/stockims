(() => {
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

  document.querySelectorAll('[data-confirm-sortie]').forEach((form) => {
    let submitted = false;
    form.addEventListener('submit', (event) => {
      if (submitted) { event.preventDefault(); return; }
      const quantity = form.querySelector('input[type="number"]')?.value;
      const article = form.dataset.article || 'cet article';
      if (!window.confirm(`Confirmer la sortie de ${quantity || 'cette quantité'} unité(s) pour « ${article} » ?`)) {
        event.preventDefault();
        return;
      }
      submitted = true;
      const button = form.querySelector('button[type="submit"]');
      if (button) { button.disabled = true; button.setAttribute('aria-busy', 'true'); button.textContent = 'Enregistrement…'; }
    });
  });
})();
