using Microsoft.EntityFrameworkCore.Migrations;

namespace Core.Db.Migrations
{
    public partial class Changeplacereviewlink : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "PlaceId2",
                schema: "citylogia",
                table: "Reviews");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<long>(
                name: "PlaceId2",
                schema: "citylogia",
                table: "Reviews",
                type: "bigint",
                nullable: false,
                defaultValue: 0L);
        }
    }
}
