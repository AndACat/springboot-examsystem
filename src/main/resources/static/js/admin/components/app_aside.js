;(function () {
    const template = '<aside class="main-sidebar sidebar-dark-primary elevation-4">' +
        '    <!-- Brand Logo -->' +
        '    <a href="/home" class="brand-link">' +
        '      <img src="../../dist/img/AdminLTELogo.png"' +
        '           alt="ExamSystem Logo"' +
        '           class="brand-image img-circle elevation-3"' +
        '           style="opacity: .8">' +
        '      <span class="brand-text font-weight-light">在线考试系统</span>' +
        '    </a>' +
        '' +
        '    <!-- Sidebar -->' +
        '    <div class="sidebar">' +
        '      <!-- Sidebar Menu -->' +
        '      <nav class="mt-2">' +
        '        <ul class="nav nav-pills nav-sidebar flex-column nav-child-indent" data-widget="treeview" role="menu" data-accordion="false">' +
        '          <!-- Add icons to the links using the .nav-icon class' +
        '               with font-awesome or any other icon font library -->' +
        '          <li class="nav-item has-treeview">' +
        '            <a href="#" class="nav-link">' +
        '              <i class="nav-icon fa fa-users"></i>' +
        '              <p>用户管理<i class="right fa fa-angle-left"></i></p>' +
        '            </a>' +
        '            <ul class="nav nav-treeview">' +
        '              <li class="nav-item">' +
        '                <a href="/admin/teacher.html" class="nav-link">' +
        '                  <i class="fa fa-circle nav-icon text-warning"></i>' +
        '                  <p>教师管理</p>' +
        '                </a>' +
        '              </li>' +
        '            </ul>' +
        '            <ul class="nav nav-treeview">' +
        '              <li class="nav-item">' +
        '                <a href="/admin/manager.html" class="nav-link">' +
        '                  <i class="fa fa-circle nav-icon text-warning"></i>' +
        '                  <p>考务管理</p>' +
        '                </a>' +
        '              </li>' +
        '            </ul>' +
        '          </li>' +
        '        </ul>' +
        '      </nav>' +
        '      <!-- /.sidebar-menu -->' +
        '    </div>' +
        '    <!-- /.sidebar -->' +
        '  </aside>';
    window.app_aside={
        template
    }
})()